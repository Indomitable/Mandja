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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.CopyDatabase;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.IAsyncTaskNotify;
import com.vmladenov.cook.core.db.DataHelper;

import java.io.File;
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

        if (DataHelper.getStorageState(this) == DataHelper.StorageState.None) {
            this.textView.setText("Unable to create database!\nCheck data storage!.");
            return;
        }

        String dbPath = Helpers.getDataHelper().getDbPathForCopy(this);
        copyDatabase(dbPath);
    }

    private void copyDatabase(String path) {
        try {
            File file = new File(path);
            if (file.exists())
                file.delete();
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
    public void onError(final Exception ex) {
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText("Error while extracting database!\n" + ex.getMessage());
                AlertDialog.Builder builder = new AlertDialog.Builder(CopyDatabaseActivity.this);
                builder.setMessage("Error while extracting database!\n" + ex.getMessage() + "\n" + "Next action will send email to the developer with needed information.");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Send Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("plain/text");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ventsislav.mladenov@gmail.com"});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Error while extracting database.");
                        StringBuilder builder = new StringBuilder();
                        builder.append("Error: " + ex.getMessage());
                        builder.append("\n");
                        builder.append("Stacktrace:\n");
                        for (StackTraceElement stack : ex.getStackTrace()) {
                            builder.append(stack.toString() + "\n");
                        }
                        builder.append("Manufacturer: " + Build.MANUFACTURER + "\n");
                        builder.append("Model: " + Build.MODEL);

                        emailIntent.putExtra(Intent.EXTRA_TEXT, builder.toString());
                        startActivity(emailIntent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         dialogInterface.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public void onProgress(int value) {
        this.progress.setProgress(value);
        textView.setText(this.getString(R.string.copy_db) + " " + value + "%");
    }
}