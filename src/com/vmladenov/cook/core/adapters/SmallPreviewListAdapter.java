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

package com.vmladenov.cook.core.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.ImageLoader;
import com.vmladenov.cook.domain.PreviewListItem;

import java.util.List;

public class SmallPreviewListAdapter extends ArrayAdapter<PreviewListItem> {
	LayoutInflater inflater;
	public ImageLoader imageLoader;

	public SmallPreviewListAdapter(Context context, List<PreviewListItem> objects) {
		super(context, R.layout.small_list_view, objects);
		inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(context);
	}

	public static class ViewHolder {
		public ImageView image;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;
		if (v == null) {
			v = inflater.inflate(R.layout.small_list_view, null);
			holder = new ViewHolder();
			holder.image = (ImageView) v.findViewById(R.id.smallImage);
			v.setTag(holder);
		} else
			holder = (ViewHolder) v.getTag();

		PreviewListItem preview = getItem(position);
		if (preview != null) {
			TextView title = (TextView) v.findViewById(R.id.txtTitle);
			final ImageView image = (ImageView) v.findViewById(R.id.smallImage);
			title.setText(preview.getTitle());
			if (preview.getCachedImage() != null)
				image.setImageDrawable(preview.getCachedImage());
			else if (preview.getThumbnailUrl() != null) {
				holder.image.setTag(preview);
				imageLoader.DisplayImage(preview, holder.image);
			}
		}

		return v;
	}
}
