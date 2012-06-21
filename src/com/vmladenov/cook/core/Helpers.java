package com.vmladenov.cook.core;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;
import com.vmladenov.cook.CustomApplication;
import com.vmladenov.cook.core.db.DataHelper;
import com.vmladenov.cook.core.html.SmallPreview;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public final class Helpers {
    public static synchronized DataHelper getDataHelper() {
        return CustomApplication.getInstance().getDataHelper();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) (context
                .getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected() && !info.isRoaming();
    }

    public static Bitmap getImage(String relativeUrl) {
        try {
            URL imageUrl = new URL(GlobalStrings.SiteUrl + relativeUrl);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();

            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
        }
        return null;
    }

    public static void setImageFromUrlAsync(ImageView image, final String relativeUrl) {
        if (relativeUrl == null || relativeUrl == "") return;
        final Handler handler = new Handler();
        new Thread(new ParameterizedRunnable<ImageView>(image) {

            @Override
            public void ParameterizedRun(ImageView imageParam) {
                final Bitmap bitmap = Helpers.getImage(relativeUrl);
                handler.post(new ParameterizedRunnable<ImageView>(imageParam) {
                    @Override
                    public void ParameterizedRun(ImageView imageParam) {
                        imageParam.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
    }

    public static void setImageInPreviewAndStoreAsync(final ImageView image,
                                                      final SmallPreview preview) {
        if (preview.imageUrl == null || preview.imageUrl == "") return;
        final Handler handler = new Handler();
        new Thread(new Runnable() {

            @Override
            public void run() {
                final Bitmap bitmap = Helpers.getSmallImage(image.getContext(), preview.imageUrl);
                // Helpers.getImage(preview.getImageUrl());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        image.setImageBitmap(bitmap);
                        preview.cachedImage = bitmap;
                    }
                });
            }
        }).start();
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static void copyDatabase(Context context, OutputStream out) throws IOException {
        AssetManager am = context.getAssets();
        byte[] b = new byte[1024];
        int i, r;
        String format = "cook%d.db";
        for (i = 1; i < 7; i++) {
            String fn = String.format(format, i);
            InputStream is = am.open(fn);
            while ((r = is.read(b)) != -1)
                out.write(b, 0, r);
            is.close();
        }
        out.close();
    }

    public synchronized static Bitmap getSmallImage(Context context, String imageUrl) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            String directory = context.getExternalFilesDir(null) + "/images/";
            File directoryFile = new File(directory);
            if (!directoryFile.exists()) directoryFile.mkdir();
            String imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            File file = new File(directory, imageName);
            if (file.exists()) {
                return readFile(directory + imageName);
            } else {
                if (isOnline(context)) {
                    Bitmap bitmap = Helpers.getImage(imageUrl);
                    saveFile(bitmap, directory + imageName);
                    return bitmap;
                } else
                    return null;
            }
        } else {
            return Helpers.getImage(imageUrl);
        }
        // return null;
    }

    private static Bitmap readFile(String path) {
        return BitmapFactory.decodeFile(path);
    }

    public static void saveFile(Bitmap bitmap, String path) {
        if (bitmap == null || bitmap.getHeight() < 1) return;
        try {
            OutputStream outputStream = new FileOutputStream(path);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            bitmap.compress(CompressFormat.JPEG, 90, bos);
            bos.flush();
            bos.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
