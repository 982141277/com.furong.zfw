package com.meiyin.erp.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.meiyin.erp.application.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 需要在Application中注册，为了要在程序启动器就监控整个程序。
 */
public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";

	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	// CrashHandler实例
	private static CrashHandler instance;

	// 程序的Context对象
	private Context mContext;

	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		if (instance == null)
			instance = new CrashHandler();
		return instance;
	}

	/**
	 * 初始化
	 */
	public void init(Context context) {

		mContext = context;

		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// 我自己处理一下
		handleException(ex);
		// 再交给系统处理
		mDefaultHandler.uncaughtException(thread, ex);
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {

		if (ex == null) {
			return false;
		}

		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		saveCatchInfo2File(ex);

		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {

		// 获取版本名称，版本号
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				// 版本名称
				infos.put("versionName", versionName);
				// 版本号
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}

		/** Android获取CPU和内存信息、网址的代码 **/

		// 通过反射获取用户硬件信息
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				// 暴力反射,获取私有信息
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 获取Log文件路径
	 * 
	 * @return
	 */
	private String getFilePath() {

		String file_dir = "";

		// SD卡是否存在
		boolean isSDCardExist = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());

		// 获取外部存储卡根目录是否存在
		// Environment.getExternalStorageDirectory()相当于File file=new
		// File("/sdcard")
		boolean isRootDirExist = Environment.getExternalStorageDirectory()
				.exists();

		// SD卡是否存在 并且 外部存储卡根目录存在
		if (isSDCardExist && isRootDirExist) {

			// SD卡根目录下的 crashlog
			file_dir = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/meiyin/crashlog/";
		} else {

			// 我的 App 的文件路径下的 crashlog
			// MyApplication.getInstance().getFilesDir()返回的路劲为/data/data/PACKAGE_NAME/files，其中的包就是我们建立的主Activity所在的包
			file_dir = MyApplication.getInstance().getFilesDir()
					.getAbsolutePath()
					+ "/crashlog/";
		}
		return file_dir;
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCatchInfo2File(Throwable ex) {

		// 定义错误信息 StringBuilder
		StringBuffer sb = new StringBuffer();

		// 在错误信息之前加上时间
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		sb.append(time + "\r\n");

		// 版本名称 版本号 硬件信息
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\r\n");

		}

		// 新建一个字符串Writer
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		// 将错误信息打印到Writer中
		ex.printStackTrace(printWriter);
		// 获取导致错误的原因
		Throwable cause = ex.getCause();
		// 循环读取所有错误原因
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();

		// 从writer中获取所有错误信息
		String result = writer.toString();
		result = result.replace("\t", "\r\n");
		sb.append(result);
		sb.append("\r\n\r\n\r\n\r\n");

		try {

			// 获取今天
			String today = new SimpleDateFormat("yyyy-MM-dd")
					.format(new Date());

			// 生成错误日志文件名
			String fileName = "crash-" + today + ".log";
			String file_dir = getFilePath();

			// 判断日志路径是否存在
			File dir = new File(file_dir);
			if (!dir.exists()) {
				// 不存在，则创建
				dir.mkdirs();
			}

			// 判断日志文件是否存在
			File file = new File(file_dir + fileName);
			if (!file.exists()) {
				// 不存在，则创建
				file.createNewFile();
			}

			// 将错误追加到日志文件中
			FileOutputStream fos = null;
			try {
				// 第二个参数为true表示追加方式
				fos = new FileOutputStream(file, true);
				fos.write(sb.toString().getBytes());
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
			return null;
		}
	}

}