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

package com.vmladenov.cook.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.CopyDatabase;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.IAsyncTaskNotify;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CopyDatabaseActivity extends Activity implements IAsyncTaskNotify {

    ProgressBar progress;
    TextView textView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.copy_database);
        this.progress = (ProgressBar)this.findViewById(R.id.progress_copy_db);
        this.textView = (TextView)this.findViewById(R.id.txt_copy_db);
        String dbPath = Helpers.getDataHelper().getDbPathForCopy(this);
        copyDatabase(dbPath);
    }

    private void copyDatabase(String path) {
        try {
            CopyDatabase copy = new CopyDatabase(this);
            copy.execute(new FileOutputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish() {
        Intent intent = new Intent();
        intent.setClass(CopyDatabaseActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onError(Exception ex) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onProgress(int value) {
        this.progress.setProgress(value);
        textView.setText(this.getString(R.string.copy_db) + " " + value + "%");
    }
}