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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import com.vmladenov.cook.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
