package com.meiyin.erp.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.meiyin.erp.R;
import com.meiyin.erp.application.APIURL;

import org.apache.http.Header;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class UpdateManager {

	public String TAG;

	private Context mContext;

	// 版本更新对话框
	private Dialog noticeDialog;

	// 下载进度对话框
	private Dialog downloadDialog;

	// 进度条与通知ui(刷新的handler和msg常量)
	private ProgressBar mProgress;

	// 下载进度
	private int progress;

	// APK下载句柄
	private RequestHandle downloadRequestHandle;;
	Boolean showConfirmDialog;

	// 构造方法
	public UpdateManager(Context context) {
		this.mContext = context;
		TAG = this.getClass().getSimpleName();
	}

	public void getVersion(TextHttpResponseHandler textHttpHandler) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.setContentEncoding("utf-8");
		if (client != null) {
			client.get(mContext, APIURL.ERP_VERSION_XML_URL, params,
					textHttpHandler);
		}
	}

	// 检查是否需要版本更新
	public void checkUpdateInfo(final Boolean showConfirmDialog) {
		this.showConfirmDialog = showConfirmDialog;

		getVersion(new TextHttpResponseHandler("gbk") {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				Log.e("lyt", responseString);
				Document doc = null;
				try {
					PackageInfo pi = mContext.getPackageManager()
							.getPackageInfo(mContext.getPackageName(), 0);
					doc = DocumentHelper.parseText(responseString);
					Element rootElt = doc.getRootElement();
					Iterator<Element> iterator = rootElt.elementIterator();
					String strLastVersionCode = "";
					String strLastVersioninfo = "";
					while (iterator.hasNext()) {
						Element element = iterator.next();
						if (element.getName() == "versionCode") {
							strLastVersionCode = element.getText();
						}
						if (element.getName() == "versionInfo"){
							strLastVersioninfo = element.getText();
						}
					}

					// 最新版本高于当前版本则需要更新
					if (Integer.parseInt(strLastVersionCode) > pi.versionCode) {
						if (showConfirmDialog) {
							showNoticeDialog(strLastVersioninfo);
						} else {
							downloadApk();
						}
					} else {
						if (!showConfirmDialog) {
							ToastUtil.showToast(mContext, "您当前已经是最新版本！");
						}
					}

				} catch (DocumentException e) {
					e.printStackTrace();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Toast.makeText(mContext, "版本信息下载失败", Toast.LENGTH_SHORT).show();
				LogUtil.e("lyt", "onFailure statusCode:" + statusCode);
				throwable.printStackTrace();
				// LogUtil.e("TextHttpResponseHandler", throwable.toString());

			}
		});
	}

	// 显示版本更新对话框
	private void showNoticeDialog(String strLastVersioninfo) {
		AlertDialog.Builder builder = new Builder(mContext);
		LinearLayout submit_dialog = (LinearLayout) LayoutInflater.from(
				mContext).inflate(R.layout.submit_dialog, null);
		TextView heads = (TextView) submit_dialog.findViewById(R.id.heads);
		TextView submit_title_message = (TextView) submit_dialog.findViewById(R.id.submit_title_message);
		Button button_t = (Button) submit_dialog
				.findViewById(R.id.button_t);
		Button button_f = (Button) submit_dialog
				.findViewById(R.id.button_f);
		submit_title_message.setText("ERP版本更新");
		heads.setTextSize(15);
		heads.setText(strLastVersioninfo);
		builder.setView(submit_dialog);
		noticeDialog = builder.create();
		noticeDialog.show();
		button_t.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				noticeDialog.dismiss();
				Uri uri = Uri.parse(APIURL.ERP_APKURL);
				Intent intent = new Intent(Intent.ACTION_VIEW,uri);
				mContext.startActivity(intent);
//				showDownloadDialog();
			}
		});
		button_f.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				noticeDialog.dismiss();
			}
		});
//		builder.setTitle("版本更新");
//		builder.setMessage(strLastVersioninfo);
//		builder.setPositiveButton("下载更新", new OnClickListener() {
//			// 点击下载时 关闭版本更新对话框 显示下载进度条对话框
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				showDownloadDialog();
//			}
//		});
//		builder.setNegativeButton("以后再说", new OnClickListener() {
//			// 点击以后再说 关闭版本更新对话框
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
	}

	// 显示下载进度条对话框
	private void showDownloadDialog() {

		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("新版本下载中...");
		final LayoutInflater inflater = LayoutInflater.from(mContext);

		View v = inflater.inflate(R.layout.dialog_update_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);

		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

				if (downloadRequestHandle != null) {
					downloadRequestHandle.cancel(true);
				}

			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();

		downloadApk();
	}

	// 下载APK
	public void downloadApk() {

		String s = FileUtil.getApkFilePath(mContext);
		File file = new File(s);

		try {
			if (!file.exists()) {
				file.createNewFile();
				DownloadApkHttpHandler fileHttpHandler = new DownloadApkHttpHandler(
						file);
				downloadRequestHandle = downloadApp(APIURL.ERP_APKURL,
						fileHttpHandler);
			} else {
				file.delete();
				file.createNewFile();
				DownloadApkHttpHandler fileHttpHandler = new DownloadApkHttpHandler(
						file);
				downloadRequestHandle = downloadApp(APIURL.ERP_APKURL,
						fileHttpHandler);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(mContext, "ERP App下载失败", Toast.LENGTH_SHORT).show();
		}
	}

	public RequestHandle downloadApp(String url,
			FileAsyncHttpResponseHandler FileHttpHandler) {
		RequestHandle requestHandle = null;
		AsyncHttpClient client = new AsyncHttpClient();
		if (client != null) {
			try {
				requestHandle = client.get(url, null, FileHttpHandler);
			} catch (Exception e) {
			}
		}
		return requestHandle;
	}

	private class DownloadApkHttpHandler extends FileAsyncHttpResponseHandler {

		File file;

		public DownloadApkHttpHandler(File file) {
			super(file);
			this.file = file;
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, File file) {
			if (file == null || !file.exists()) {
				Log.i(TAG, "FileHttpHandler onSuccess file==null!");
				return;
			}
			// 将下载下来的apk文件保存到sdcard中（要做sdcard是否存在的判断）
//			Log.e("lyt", " FileHttpHandler onSuccess file.getAbsolutePath():"
//					+ file.getAbsolutePath());
//			if (Build.VERSION.SDK_INT >= 23) {
//				String cmdString = "chmod 777 " + file.getAbsolutePath();
//				try {
//					Process p = Runtime.getRuntime().exec(cmdString);
//					if (p.waitFor() == 0) {
//						Log.e("lyt", "权限修改成功");
//						// 跳出apk安装的界面
//						ApkUtil.installApk(mContext, file.getAbsolutePath());
//					} else {
//						Log.e("lyt", "权限修改失败");
//					}
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//			} else {
				// if (FileUtil.hasSDCard()) {
				ApkUtil.installApk(mContext, file.getAbsolutePath());
				// } else {
				// String cmdString = "chmod 777 " + file.getAbsolutePath();
				// try {
				// Process p = Runtime.getRuntime().exec(cmdString);
				// if (p.waitFor() == 0) {
				// Log.e("lyt", "权限修改成功");
				// // 跳出apk安装的界面
				// ApkUtil.installApk(mContext, file.getAbsolutePath());
				// } else {
				// Log.e("lyt", "权限修改失败");
				// }
				// } catch (Exception e) {
				// // TODO: handle exception
				// }
				// }
//
//			}

		}

		@Override
		public void onProgress(int bytesWritten, int totalSize) {
			super.onProgress(bytesWritten, totalSize);

			progress = (int) (((float) bytesWritten / totalSize) * 100);
			if (showConfirmDialog)
				mProgress.setProgress(progress);
		}

		@Override
		public void onCancel() {
			super.onCancel();
			deleteFile();
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, File file) {
			Toast.makeText(mContext, "ERPApp下载失败，请重新下载", Toast.LENGTH_SHORT)
					.show();
			LogUtil.i("FileHttpHandler", throwable.toString());
			deleteFile();
		}

		@Override
		public void onFinish() {
			super.onFinish();
		}

		private void deleteFile() {
			if (file.exists()) {
				file.delete();
			}
		}
	}
}