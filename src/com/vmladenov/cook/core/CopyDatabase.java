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

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

//import org.tukaani.xz.LZMA2Options;
//import org.tukaani.xz.XZInputStream;

public class CopyDatabase extends AsyncTask<OutputStream, Integer, Integer> {

	private Context context;
    private IAsyncTaskNotify notify;

	public CopyDatabase(Context context) {
		this.context = context;
        this.notify = this.context instanceof IAsyncTaskNotify ? (IAsyncTaskNotify)this.context : null;
	}

	protected Integer doInBackground(OutputStream... outputs) {
		try {
			OutputStream out = outputs[0];
			AssetManager am = context.getAssets();
			//byte[] buf = new byte[4096];
            byte[] buf = new byte[8192];
            int size = -1;
            int i = 1;
            int count = 4170;
            //XZInputStream xzInputStream = new XZInputStream(am.open("cook_compressed.db"));
            GZIPInputStream inputStream = new GZIPInputStream(am.open("cook_compressed.db"));
            while ((size = inputStream.read(buf)) != -1) {
                out.write(buf, 0, size);
                publishProgress((int) ((i / (float) count) * 100));
                i++;
            }
            inputStream.close();
            out.close();
		} catch (Exception e) {
            if (notify != null)
                notify.onError(e);
			return -1;
        }
        if (notify != null)
            notify.onFinish();
		return 0;
	}

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (notify != null)
            notify.onProgress(values[0]);
    }
}
