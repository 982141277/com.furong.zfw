package com.meiyin.erp.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.meiyin.erp.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Image {
	public static void initImageLoader(Context context) {

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.mipmap.loading)
				.showImageForEmptyUri(R.mipmap.loading)
				.showImageOnFail(R.mipmap.loading).cacheInMemory(true)
				.cacheOnDisk(true).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.denyCacheImageMultipleSizesInMemory()
				.diskCache(
						new UnlimitedDiscCache(new File(
								getImageFileDir(context))))
				//
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .writeDebugLogs() // Remove for release app
				.defaultDisplayImageOptions(defaultOptions).build();

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public static String getImageFileDir(Context context) {
		File file1 = new File(getAppRootDir(context) + "/image");
		if (file1 == null || !file1.exists()) {
			file1.mkdir();
		}
		return getAppRootDir(context) + "/image";
	}

	public static String getAppRootDir(Context context) {
		String appRootDir;
		if (hasSDCard()) {
			appRootDir = Environment.getExternalStorageDirectory() + "/demo";
		} else {
			appRootDir = "/storage/demo";
		}
		File file = new File(appRootDir);
		if (!file.exists()) {
			file.mkdir();
		}
		return appRootDir;
	}

	public static boolean hasSDCard() {
		boolean b = false;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			b = true;
		}
		return b;
	}

	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 256)
					&& (options.outHeight >> i <= 256)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	public static void ImageLoader(String image, ImageView img) {

		ImageLoader.getInstance().displayImage(image, img,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						Log.e("uri", "imageUriStart:" + imageUri);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						Log.e("uri", "imageUriFailed:" + imageUri + "--fails--"
								+ failReason);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {

					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {

					}
				});
	}
	
}
