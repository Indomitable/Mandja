package com.vmladenov.cook.core;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.vmladenov.cook.CustomApplication;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.db.DataHelper;

public final class Helpers
{
	public static synchronized DataHelper getDataHelper()
	{
		return CustomApplication.getInstance().getDataHelper();
	}

	public static boolean isOnline(Context context)
	{
		try
		{
			SharedPreferences preferences = context.getSharedPreferences("MandjaSettings", Context.MODE_PRIVATE);
			boolean useOnlyWiFi = preferences.getBoolean("DownloadOnlyOnWiFi", false);

			ConnectivityManager cm = (ConnectivityManager) (context.getSystemService(Context.CONNECTIVITY_SERVICE));
			if (useOnlyWiFi) {
				NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				return wifiInfo.isConnected();
			}

			NetworkInfo info = cm.getActiveNetworkInfo();
			return info != null && info.isConnected() && !info.isRoaming();
		} catch (Exception e) {
			return false;
		}
	}

	public static BitmapDrawable getImage(String url)
	{
		try
		{
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			return new BitmapDrawable(is);
			// return BitmapFactory.decodeStream(is);

		} catch (IOException e)
		{
		}
		return null;
	}

	public static void setImageFromUrlAsync(IOnImageDownload callback, Context context, final String url)
	{
		if (url == null || url == "")
			return;
		if (!Helpers.isOnline(context)) {
			return;
		}
		final Handler handler = new Handler();
		new Thread(new ParameterizedRunnable<IOnImageDownload>(callback)
		{

			@Override
			public void ParameterizedRun(IOnImageDownload callback)
			{
				final BitmapDrawable draw = Helpers.getImage(url);
				handler.post(new ParameterizedRunnable<IOnImageDownload>(callback)
				{
					@Override
					public void ParameterizedRun(IOnImageDownload callback)
					{
						callback.ReceiveImage(draw);
					}
				});
			}
		}).start();
	}

	public static void saveFile(BitmapDrawable draw, String path)
	{
		if (draw == null || draw.getBitmap() == null || draw.getBitmap().getHeight() < 1)
			return;
		try
		{
			OutputStream outputStream = new FileOutputStream(path);
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			draw.getBitmap().compress(CompressFormat.JPEG, 90, bos);
			bos.flush();
			bos.close();
			outputStream.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void copyDatabase(Context context, OutputStream out) throws IOException
	{
		ProgressDialog progressDialog = ProgressDialog.show(context, context.getString(R.string.loading), context.getString(R.string.initialize),
				false);
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
		progressDialog.dismiss();
	}

}
