/*
 * Copyright (C) 2011 Ventsislav Mladenov <ventsislav dot mladenov at gmail dot com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.vmladenov.cook.core;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.SparseArray;
import android.widget.ImageView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.iterateHelpers.Find;
import com.vmladenov.cook.core.iterateHelpers.IPredicate;
import com.vmladenov.cook.domain.PreviewListItem;

import java.io.File;
import java.util.Collection;
import java.util.Stack;

public class ImageLoader {
	// the simplest in-memory cache implementation. This should be replaced with
	// something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
	private SparseArray<BitmapDrawable> cache = new SparseArray<BitmapDrawable>();

	private File cacheDir;

	private final Context context;
	private final Boolean cacheImages;
	private final Boolean downloadThumbnails;

	public ImageLoader(Context context) {
		this.context = context;
		photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);
		cacheDir = getCacheDir();
		SharedPreferences preferences = context.getSharedPreferences(
				"MandjaSettings", Context.MODE_PRIVATE);
		cacheImages = preferences.getBoolean("CacheImages", true);
		downloadThumbnails = preferences.getBoolean("DownloadThumbnails", true);
	}

	private File getCacheDir() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			String directory = context.getExternalFilesDir(null) + "/images/";
			File directoryFile = new File(directory);
			if (!directoryFile.exists())
				directoryFile.mkdir();
			return directoryFile;
		}
		return null;
	}

	final int stub_id = R.drawable.icon;

	public void DisplayImage(PreviewListItem preview, ImageView imageView) {
		BitmapDrawable draw = cache.get(preview.getId());
		if (draw != null) {
			imageView.setImageDrawable(draw);
		} else {
			queuePhoto(preview, imageView);
			imageView.setImageResource(stub_id);
		}
	}

	private void queuePhoto(PreviewListItem preview, ImageView imageView) {
		// This ImageView may be used for other images before. So there may be
		// some old tasks in the queue. We need to discard them.
		photosQueue.clean(imageView);
		PhotoToLoad p = new PhotoToLoad(preview, imageView);
		synchronized (photosQueue.photosToLoad) {
			photosQueue.photosToLoad.push(p);
			photosQueue.photosToLoad.notifyAll();
		}

		// start thread if it's not started yet
		if (photoLoaderThread.getState() == Thread.State.NEW)
			photoLoaderThread.start();
	}

	public BitmapDrawable getBitmap(String imageUrl) {
		if (!downloadThumbnails)
			return null;
		if (cacheImages && cacheDir != null) {
			String imageName = imageUrl
					.substring(imageUrl.lastIndexOf('/') + 1);
			File file = new File(cacheDir, imageName);
			if (file.exists()) {
				return new BitmapDrawable(cacheDir + "/" + imageName);
			} else {
				if (Helpers.isOnline(context)) {
					BitmapDrawable draw = Helpers.getImage(context, imageUrl);
					Helpers.saveFile(draw, cacheDir + "/" + imageName);
					return draw;
				} else
					return null;
			}
		} else {
			if (Helpers.isOnline(context)) {
				return Helpers.getImage(context, imageUrl);
			} else
				return null;
		}
	}

	// Task for the queue
	private class PhotoToLoad {
		public PreviewListItem preview;
		public ImageView imageView;

		public PhotoToLoad(PreviewListItem preview, ImageView i) {
			this.preview = preview;
			imageView = i;
		}
	}

	PhotosQueue photosQueue = new PhotosQueue();

	public void stopThread() {
		photoLoaderThread.interrupt();
	}

	// stores list of photos to download
	class PhotosQueue {
		private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

		// removes all instances of this ImageView
		public void clean(final ImageView image) {
			synchronized (photosToLoad) {
				try {
					Collection<PhotoToLoad> photos = Find.where(photosToLoad, new IPredicate<PhotoToLoad>() {
                        @Override
                        public boolean predicate(PhotoToLoad value) {
                            return value.imageView.equals(image);
                        }
                    });
                    photosToLoad.removeAll(photos);
				} catch (ArrayIndexOutOfBoundsException e) {

				}
			}
		}
	}

	class PhotosLoader extends Thread {
		public void run() {
			try {
				while (true) {
					// thread waits until there are any images to load in the
					// queue
					if (photosQueue.photosToLoad.size() == 0)
						synchronized (photosQueue.photosToLoad) {
							photosQueue.photosToLoad.wait();
						}
					if (photosQueue.photosToLoad.size() != 0) {
						PhotoToLoad photoToLoad;
						synchronized (photosQueue.photosToLoad) {
							photoToLoad = photosQueue.photosToLoad.pop();
						}
						BitmapDrawable draw = getBitmap(photoToLoad.preview
								.getThumbnailUrl());
						if (draw == null)
							continue;
						cache.put(photoToLoad.preview.getId(), draw);
						Object tag = photoToLoad.imageView.getTag();
						if (tag != null
								&& ((PreviewListItem) tag)
										.equals(photoToLoad.preview)) {
							BitmapDisplayer bd = new BitmapDisplayer(draw,
									photoToLoad.imageView);
							Activity a = (Activity) photoToLoad.imageView
									.getContext();
							a.runOnUiThread(bd);
						}
					}
					if (Thread.interrupted())
						break;
				}
			} catch (InterruptedException e) {
				// allow thread to exit
			}
		}
	}

	PhotosLoader photoLoaderThread = new PhotosLoader();

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		BitmapDrawable bitmap;
		ImageView imageView;

		public BitmapDisplayer(BitmapDrawable b, ImageView i) {
			bitmap = b;
			imageView = i;
		}

		public void run() {
			if (bitmap != null)
				imageView.setImageDrawable(bitmap);
			else
				imageView.setImageResource(stub_id);
		}
	}

	public void clearCache() {
		// clear memory cache
		cache.clear();

		// clear SD cache
		File[] files = cacheDir.listFiles();
		for (File f : files)
			f.delete();
	}

}
