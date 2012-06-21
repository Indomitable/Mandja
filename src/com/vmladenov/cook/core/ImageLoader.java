package com.vmladenov.cook.core;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.html.SmallPreview;

import java.io.File;
import java.util.HashMap;
import java.util.Stack;

public class ImageLoader {
    // the simplest in-memory cache implementation. This should be replaced with
    // something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
    private HashMap<SmallPreview, Bitmap> cache = new HashMap<SmallPreview, Bitmap>();

    private File cacheDir;

    private final Context context;
    private final Boolean cacheImages;

    public ImageLoader(Context context) {
        this.context = context;
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);
        cacheDir = getCacheDir();
        SharedPreferences preferences = context.getSharedPreferences("MandjaSettings", Context.MODE_PRIVATE);
        cacheImages = preferences.getBoolean("CacheImages", true);
    }

    private File getCacheDir() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            String directory = context.getExternalFilesDir(null) + "/images/";
            File directoryFile = new File(directory);
            if (!directoryFile.exists()) directoryFile.mkdir();
            return directoryFile;
        }
        return null;
    }

    final int stub_id = R.drawable.icon;

    public void DisplayImage(SmallPreview preview, ImageView imageView) {
        if (cache.containsKey(preview))
            imageView.setImageBitmap(cache.get(preview));
        else {
            queuePhoto(preview, imageView);
            imageView.setImageResource(stub_id);
        }
    }

    private void queuePhoto(SmallPreview preview, ImageView imageView) {
        // This ImageView may be used for other images before. So there may be
        // some old tasks in the queue. We need to discard them.
        photosQueue.clean(imageView);
        PhotoToLoad p = new PhotoToLoad(preview, imageView);
        synchronized (photosQueue.photosToLoad) {
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }

        // start thread if it's not started yet
        if (photoLoaderThread.getState() == Thread.State.NEW) photoLoaderThread.start();
    }

    public Bitmap getBitmap(String imageUrl) {
        if (cacheImages && cacheDir != null) {
            String imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            File file = new File(cacheDir, imageName);
            if (file.exists()) {
                return BitmapFactory.decodeFile(cacheDir + "/" + imageName);
            } else {
                if (Helpers.isOnline(context)) {
                    Bitmap bitmap = Helpers.getImage(imageUrl);
                    Helpers.saveFile(bitmap, cacheDir + "/" + imageName);
                    return bitmap;
                } else
                    return null;
            }
        } else {
            return Helpers.getImage(imageUrl);
        }
    }

    // Task for the queue
    private class PhotoToLoad {
        public SmallPreview preview;
        public ImageView imageView;

        public PhotoToLoad(SmallPreview preview, ImageView i) {
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
        public void clean(ImageView image) {
            synchronized (photosToLoad) {
                try {
                    int index = photosToLoad.indexOf(image);
                    while (index > -1) {
                        photosToLoad.remove(index);
                        index = photosToLoad.indexOf(image);
                    }
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
                        Bitmap bmp = getBitmap(photoToLoad.preview.imageUrl);
                        cache.put(photoToLoad.preview, bmp);
                        Object tag = photoToLoad.imageView.getTag();
                        if (tag != null && ((SmallPreview) tag).equals(photoToLoad.preview)) {
                            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad.imageView);
                            Activity a = (Activity) photoToLoad.imageView.getContext();
                            a.runOnUiThread(bd);
                        }
                    }
                    if (Thread.interrupted()) break;
                }
            } catch (InterruptedException e) {
                // allow thread to exit
            }
        }
    }

    PhotosLoader photoLoaderThread = new PhotosLoader();

    // Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        ImageView imageView;

        public BitmapDisplayer(Bitmap b, ImageView i) {
            bitmap = b;
            imageView = i;
        }

        public void run() {
            if (bitmap != null)
                imageView.setImageBitmap(bitmap);
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
