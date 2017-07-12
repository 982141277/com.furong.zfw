package com.jcd.psms.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.jcd.psms.Application.PsmsApplication;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
	//预定SD卡读写位置
	public static final String APP_BUF_ROOT_DIR = Environment
			.getExternalStorageDirectory() + "/psms";

	public static String gethtmlpath(){

		String	appRootDir = PsmsApplication.getInstance().getFilesDir()
				.getAbsolutePath()+"/psms";
		File file = new File(appRootDir);
		if (file == null || !file.exists()) {
			file.mkdir();
		}
		return appRootDir;
	}
	/**
	 * 根据原始rar路径，解压到指定文件夹下.
	 * @param srcRarPath 原始rar路径
	 * @param dstDirectoryPath 解压到的文件夹
	 */
	public static boolean unRarFile(String srcRarPath, String dstDirectoryPath) {
		if (!srcRarPath.toLowerCase().endsWith(".rar")) {
			System.out.println("非rar文件！");
			return false;
		}
		File dstDiretory = new File(dstDirectoryPath);
		if (!dstDiretory.exists()) {// 目标目录不存在时，创建该文件夹
			dstDiretory.mkdirs();
		}
		Archive a = null;
		try {

			a = new Archive(new File(srcRarPath));
			if (a != null) {
				//a.getMainHeader().print(); // 打印文件信息.
				FileHeader fh = a.nextFileHeader();

				while (fh != null) {

					//防止文件名中文乱码问题的处理
					String fileName = fh.getFileNameW().isEmpty()?fh.getFileNameString():fh.getFileNameW();
					fileName=fileName.replace("\\","/");
					if (fh.isDirectory()) { // 文件夹
						File fol = new File(dstDirectoryPath + File.separator + fileName);
						fol.mkdirs();
					} else { // 文件
						File out = new File(dstDirectoryPath +File.separator+ fileName.trim());

						try {
							if (!out.exists()) {
								if (!out.getParentFile().exists()) {// 相对路径可能多级，可能需要创建父目录.
									out.getParentFile().mkdirs();
								}
								out.createNewFile();
							}
							FileOutputStream os = new FileOutputStream(out);
							a.extractFile(fh, os);
							os.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					fh = a.nextFileHeader();
				}
				a.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	public static String getAppRootDir(Context context) {
		String appRootDir;
//		if (Build.VERSION.SDK_INT >= 23) {
		// 自动获取项目的存储位置
		appRootDir = PsmsApplication.getInstance().getFilesDir()
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
//				appRootDir = PsmsApplication.getInstance().getFilesDir()
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
	 * @param
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
