package com.vmladenov.cook.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.vmladenov.cook.R;

public class CopyDatabase extends AsyncTask<OutputStream, Void, Integer> {

	private ProgressDialog progressDialog;
	private Context context;

	public CopyDatabase(Context context) {
		this.context = context;
		// progressDialog = new ProgressDialog(context);
		// progressDialog.setMessage(context.getString(R.string.initialize));
		// progressDialog.setTitle(R.string.loading);
	}

	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(this.context);
		progressDialog.setMessage(context.getString(R.string.initialize));
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	protected Integer doInBackground(OutputStream... outputs) {
		try {
			OutputStream out = outputs[0];
			AssetManager am = context.getAssets();
			byte[] b = new byte[4096];
			ZipInputStream zipStream = new ZipInputStream(am.open("cook.db.zip"));
			ZipEntry zipEntry = zipStream.getNextEntry();
			if (zipEntry != null)
			{
				for (int r = zipStream.read(b); r != -1; r = zipStream.read(b))
					out.write(b, 0, r);
				zipStream.closeEntry();
			}
			zipStream.close();
			out.close();
		} catch (IOException e) {
			return -1;
		}
		return 0;
	}

	@Override
	protected void onPostExecute(Integer result) {
		progressDialog.dismiss();
	}

}
