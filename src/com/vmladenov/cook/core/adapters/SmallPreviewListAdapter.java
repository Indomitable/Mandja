package com.vmladenov.cook.core.adapters;

import java.util.List;

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
