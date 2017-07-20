package com.meiyin.erp.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

public class ApkUtil {
	public static boolean AUTO_UNINSTALL=false;//是否是自动卸载
	/**
	 * 通过系统下载器去下载apk
	 * @param context
	 * @param downloadUrl
	 * @return
	 */
//public static long downloadApk(Context context, String downloadUrl) {
//
//		DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//
//		Uri uri = Uri.parse(downloadUrl);
//		Request request = new Request(uri);
//		// 设置允许使用的网络类型，这里是移动网络和wifi都可以
//		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
//		// 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
//		// request.setShowRunningNotification(false);
//		// 不显示下载界面
//		request.setVisibleInDownloadsUi(true);
//
//		// request.setDestinationInExternalFilesDir(this, null, "tar.apk");
//		long id = downloadManager.enqueue(request);
//		return id;
//	}
	/**
	 * 描述: 安装									 
	 * 最后修改时间:2015年3月8日 下午9:07:50
	 */
	public static boolean install(String apkPath,Context context){
		// 先判断手机是否有root权限
		if(hasRootPerssion()){
			// 有root权限，利用静默安装实现
			return clientInstall(apkPath,context);
		}else{
			// 没有root权限，利用意图进行安装
			File file = new File(apkPath);
			if(!file.exists())
				return false; 
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
			context.startActivity(intent);
			return true;
		}
	}
	
	/**
	 * 描述: 卸载											 
	 * 最后修改时间:2015年3月8日 下午9:07:50
	 */
	public static boolean uninstall(String packageName,Context context){
		if(hasRootPerssion()){
			// 有root权限，利用静默卸载实现
			return clientUninstall(packageName);
		}else{
			Uri packageURI = Uri.parse("package:" + packageName);
			Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,packageURI);
			uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(uninstallIntent);
			return true;
		}
	}
	
	/**
	 * 判断手机是否有root权限
	 */
	private static boolean hasRootPerssion(){
		PrintWriter PrintWriter = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("su");
			PrintWriter = new PrintWriter(process.getOutputStream());
			PrintWriter.flush();
			PrintWriter.close();
			int value = process.waitFor();  
			return returnResult(value);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(process!=null){
				process.destroy();
			}
		}
		return false;
	}
	
	/**
	 * 静默安装
	 * 测试没通过。。
	 */
	private static boolean clientInstall(final String apkPath,final Context context){
		
		new Thread() {
		    public void run() {
		        Process process = null;
		        OutputStream out = null;
		        InputStream in = null;
		        try {
		            // 请求root
		            process = Runtime.getRuntime().exec("su");
		            out = process.getOutputStream();
		            // 调用安装
		            System.out.println(apkPath);
		            out.write(("pm install -r " + apkPath + "").getBytes());
		            in = process.getInputStream();
		            int len = 0;
		            int readLen = 0;
		            byte[] bs = new byte[256];
		            //读出所有的输出数据
		            while (-1 != (readLen = in.read(bs))) {
		                len = len + readLen;
		                //如果读的数据大于缓存区。则停止
		                if (len > bs.length) {
		                    len -= readLen;
		                    break;
		                }
		            }
		            String state = new String(bs, 0, len);
		            if (state.startsWith("Success")) {
		            // 安装成功后的操作
		    				Toast.makeText(context, "ERP安装成功", Toast.LENGTH_SHORT).show();
		    				Log.e("lyt", "安装成功！");

		    				

		            } 
		             else {
		            	 Log.e("lyt", "安装失败！手动安装吧");
		                //静默安装失败，使用手动安装
		            	 installApk(context,apkPath);
		            } 
		         } catch (IOException e) {
		             e.printStackTrace();
		         } catch (Exception e) {
		            e.printStackTrace();
		         } finally {
		            try {
		                if (out != null) {
		                out.flush();
		                out.close();
		            }
		            if (in != null) {
		                in.close();
		            }
		            } catch (IOException e) {
		            e.printStackTrace();
		            }
		        }
		    }
		}.start();
		
		return false;
	}
	
	/**
	 * 静默卸载
	 */
	private static boolean clientUninstall(String packageName){
		PrintWriter PrintWriter = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("su");
			PrintWriter = new PrintWriter(process.getOutputStream());
			PrintWriter.println("LD_LIBRARY_PATH=/vendor/lib:/system/lib ");
			PrintWriter.println("pm uninstall "+packageName);
			PrintWriter.flush();
			PrintWriter.close();
			int value = process.waitFor();  
			return returnResult(value); 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(process!=null){
				process.destroy();
			}
		}
		return false;
	}
	
	/**
	 * 启动app
	 * com.exmaple.client/.MainActivity
	 * com.exmaple.client/com.exmaple.client.MainActivity
	 */
	public static boolean startApp(String packageName,String activityName){
		boolean isSuccess = false;
		String cmd = "am start -n " + packageName + "/" + activityName + " \n";
		Process process = null;
		try {
		   process = Runtime.getRuntime().exec(cmd);
		   int value = process.waitFor();  
	       return returnResult(value);
		} catch (Exception e) {
		  e.printStackTrace();
		} finally{
			if(process!=null){
				process.destroy();
			}
		}
		return isSuccess;
	}
	
	
	private static boolean returnResult(int value){
		// 代表成功  
		if (value == 0) {
			return true;
		} else if (value == 1) { // 失败
			return false;
		} else { // 未知情况
			return false;
		}  
	}
	/**
	 * @param apkUrl
	 *            安装APP
	 * @param context
	 * @param apkUrl
	 *            apk的存放路径
	 */
	public static Boolean installApk(Context context, String apkUrl) {
		LogUtil.i("ApkUtil","installApkinstallApk-----" + apkUrl + "");
		if (apkUrl == null || apkUrl.trim().equals("")) {
			return false;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		File file = new File(apkUrl);
		if(file.exists()){
			i.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
			context.startActivity(i);
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 判断程序是否在前台运行
	 * 
	 * @param packageName
	 * @return
	 */
	public static boolean isOpenFront(Context context, String packageName) {
		//LogUtil.i("ApkUtil", "----------isOpen------- packageName:"+packageName);
		if (packageName.equals("") | packageName == null)
			return false;
		//LogUtil.i("ApkUtil", "isScreenLocked(context):"+isScreenLocked(context));
		if(isScreenLocked(context)){
			//LogUtil.i("ApkUtil", "isScreenLocked==true");
			return false;
		}
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
		for (RunningAppProcessInfo runinfo : runningAppProcesses) {
			String pn = runinfo.processName;
			if (pn.equals(packageName) && runinfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
				//LogUtil.i("ApkUtil", " run foreground");
				return true;
			}
		}
		//LogUtil.i("ApkUtil", "have not run foreground");
		return false;
	}
	

    public final static boolean isScreenLocked(Context c) {
        android.app.KeyguardManager mKeyguardManager = (android.app.KeyguardManager) c.getSystemService(c.KEYGUARD_SERVICE);
        
        //mKeyguardManager.inKeyguardRestrictedInputMode();
        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }

	public static void uninstallApp(Context context,String packageName){
		//通过程序的包名创建URI 
		Uri packageURI = Uri.parse("package:"+packageName); 
		//创建Intent意图 
		Intent intent = new Intent(Intent.ACTION_DELETE,packageURI); 
		//执行卸载程序 
		context.startActivity(intent);
		AUTO_UNINSTALL = true;
	}
	
}
