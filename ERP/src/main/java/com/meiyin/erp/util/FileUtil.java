package com.meiyin.erp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.meiyin.erp.application.MyApplication;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
	public static final String APP_BUF_ROOT_DIR = Environment
			.getExternalStorageDirectory() + "/meiyin";

	public static String getAppRootDir(Context context) {
		String appRootDir;
//		if (Build.VERSION.SDK_INT >= 23) {
			// 自动获取项目的存储位置
			appRootDir = MyApplication.getInstance().getFilesDir()
					.getAbsolutePath();
//		} else {
//			if (hasSDCard()) {
//				// 有SD卡(没有外置SD卡，有内置的SD卡也会进到这里)
//				appRootDir = APP_BUF_ROOT_DIR;
//				File file = new File(appRootDir);
//				if (!file.exists()) {
//					file.mkdir();
//				}
//			} else {
//				// 自动获取项目的存储位置
//				appRootDir = MyApplication.getInstance().getFilesDir()
//						.getAbsolutePath();
//			}
//		}
		return appRootDir;
	}

	public static File GetFileRootPath(Context context) throws Exception {
		File path = null;
		if (hasSDCard()) {// sd卡可以操作
			path = Environment.getExternalStorageDirectory();// sd卡根路径
		} else {
			path = context.getFilesDir();// 手机存储默认路径
		}
		return path;
	}

	/**
	 * 根据apk的下载地址返回apk下载下来之后存放的路径
	 * 
	 * @paramurl
	 */
	public static String getApkFilePath(Context context) {
		// File file1 = new File(getAppRootDir(context));
		// if (file1 == null || !file1.exists()) {
		// file1.mkdir();
		// }
		return getAppRootDir(context) + "/ERP.apk";
	}

	/**
	 * 根据文件的路径判断文件是否存在
	 * 
	 * @param apkFileDir
	 * @return
	 */
	public static Boolean isExist(String apkFileDir) {
		File file = new File(apkFileDir);
		if (file.exists())
			return true;
		return false;
	}

	/**
	 * 判断是否有sdcard
	 * 
	 * @return
	 */
	public static boolean hasSDCard() {
		boolean b = false;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			b = true;
		}
		return b;
	}

	public static void saveBitmap(Bitmap bm, String picName) {
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(APP_BUF_ROOT_DIR, picName + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(APP_BUF_ROOT_DIR + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(APP_BUF_ROOT_DIR + fileName);
		file.isFile();
		return file.exists();
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
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
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

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

	public static void delFile(Context context, String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}
}
