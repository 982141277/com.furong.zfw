package com.jcd.psms.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;


public class UpdateVersion {

		private Context mContext;
		// 下载进度
		private int progress;
		// APK下载句柄
		private RequestHandle downloadRequestHandle;;
		private SharedPreferences sp;
		private String Tag=this.getClass().getSimpleName();

		// 构造方法
		public UpdateVersion(Context context) {
			this.mContext = context;
			sp = mContext.getSharedPreferences("psmsversion", Context.MODE_PRIVATE);
		}

		public void getVersion(TextHttpResponseHandler textHttpHandler) {
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.setContentEncoding("utf-8");
			if (client != null) {
				client.get(mContext, APIUtil.PSMS_VERSION_XML_URL, params,
						textHttpHandler);
			}
		}
		// 检查是否需要版本更新
		public void checkUpdateInfo() {

			getVersion(new TextHttpResponseHandler("gbk") {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
									  String responseString) {
					Document doc = null;
					LogUtil.e(Tag,responseString);
					try {
						PackageInfo pi = mContext.getPackageManager()
								.getPackageInfo(mContext.getPackageName(), 0);
						doc = DocumentHelper.parseText(responseString);
						Element rootElt = doc.getRootElement();
						Iterator<Element> iterator = rootElt.elementIterator();
//						String strLastVersionCode = "";
//						String strLastVersioninfo = "";
						String html = "";
						String css = "";
						String js = "";
						String fonts = "";
						String img = "";
						while (iterator.hasNext()) {
							Element element = iterator.next();
//							if (element.getName() == "versionCode") {
//								strLastVersionCode = element.getText();
//							}
//							if (element.getName() == "versionInfo"){
//								strLastVersioninfo = element.getText();
//							}
							if (element.getName() == "html"){
								html = element.getText();
							}
							if (element.getName() == "css"){
								css = element.getText();
							}
							if (element.getName() == "js"){
								js = element.getText();
							}
							if (element.getName() == "fonts"){
								fonts = element.getText();
							}
							if (element.getName() == "img"){
								img = element.getText();
							}
						}

						// 最新版本高于当前版本则需要更新
						if (Integer.parseInt(html) >sp.getInt("html",0)) {
							downloadFile("/html.rar",Integer.parseInt(html),"html");
						}
						if(Integer.parseInt(css) >sp.getInt("css",0)) {
							downloadFile("/css.rar",Integer.parseInt(css),"css");
						}
						if(Integer.parseInt(js) >sp.getInt("js",0)) {
							downloadFile("/js.rar",Integer.parseInt(js),"js");
						}
						if(Integer.parseInt(fonts) >sp.getInt("fonts",0)) {
							downloadFile("/fonts.rar",Integer.parseInt(fonts),"fonts");
						}
						if(Integer.parseInt(img) >sp.getInt("img",0)) {
							downloadFile("/img.rar",Integer.parseInt(img),"img");
						}else{
							LogUtil.e("lyt","检查版本后!您当前已经是最新版本！");

						}
//						// 最新版本高于当前版本则需要更新
//						if (Integer.parseInt(strLastVersionCode) > pi.versionCode) {
//
//						} else {
//
//
//						}

					} catch (DocumentException e) {
						e.printStackTrace();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
									  String responseString, Throwable throwable) {
					LogUtil.e("lyt", "版本信息下载失败:onFailure statusCode:" + statusCode);
					throwable.printStackTrace();
				}
			});
		}

//		// 显示版本更新对话框
//		private void showNoticeDialog(String strLastVersioninfo) {
//			AlertDialog.Builder builder = new Builder(mContext);
//			LinearLayout submit_dialog = (LinearLayout) LayoutInflater.from(
//					mContext).inflate(R.layout.submit_dialog, null);
//			TextView heads = (TextView) submit_dialog.findViewById(R.id.heads);
//			TextView submit_title_message = (TextView) submit_dialog.findViewById(R.id.submit_title_message);
//			Button button_t = (Button) submit_dialog
//					.findViewById(R.id.button_t);
//			Button button_f = (Button) submit_dialog
//					.findViewById(R.id.button_f);
//			submit_title_message.setText("ERP版本更新");
//			heads.setTextSize(15);
//			heads.setText(strLastVersioninfo);
//			builder.setView(submit_dialog);
//			noticeDialog = builder.create();
//			noticeDialog.show();
//			button_t.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					noticeDialog.dismiss();
//					Uri uri = Uri.parse(APIURL.ERP_APKURL);
//					Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//					mContext.startActivity(intent);
////				showDownloadDialog();
//				}
//			});
//			button_f.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					noticeDialog.dismiss();
//				}
//			});
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
//		}

//		// 显示下载进度条对话框
//		private void showDownloadDialog() {
//
//			AlertDialog.Builder builder = new Builder(mContext);
//			builder.setTitle("新版本下载中...");
//			final LayoutInflater inflater = LayoutInflater.from(mContext);
//
//			View v = inflater.inflate(R.layout.dialog_update_progress, null);
//			mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
//
//			builder.setView(v);
//			builder.setNegativeButton("取消", new OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//
//					if (downloadRequestHandle != null) {
//						downloadRequestHandle.cancel(true);
//					}
//
//				}
//			});
//			downloadDialog = builder.create();
//			downloadDialog.show();
//
//			downloadApk();
//		}

		// 下载资源文件
		public void downloadFile(String urls,int code,String spname) {
			LogUtil.e("lyt","更新文件："+spname);
			String path = FileUtil.gethtmlpath();
			File file = new File(path+urls);
			try {
			 if (file == null || !file.exists()) {
					 file.createNewFile();
			 }else{
					 file.delete();
					 file.createNewFile();
			 }
			DownloadFileHttpHandler fileHttpHandler = new DownloadFileHttpHandler(
                    file,code,spname);
			downloadRequestHandle = downloadFile(APIUtil.PSMS_UPDATE+urls,
                    fileHttpHandler);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//下载文件
		public RequestHandle downloadFile(String url, FileAsyncHttpResponseHandler FileHttpHandler) {
			RequestHandle requestHandle = null;
			AsyncHttpClient client = new AsyncHttpClient();
			if (client != null) {
				try {
					requestHandle = client.get(url, null, FileHttpHandler);
				} catch (Exception e) {
					LogUtil.e("lyt", "下载异常");
				}
			}
			return requestHandle;
		}

		private class DownloadFileHttpHandler extends FileAsyncHttpResponseHandler {
			protected  File file;
			protected  int code;
			protected  String spname;
			public DownloadFileHttpHandler(File file,int code,String spname) {
				super(file);
				this.file = file;
				this.code=code;
				this.spname=spname;
			}

			@Override
			public void onSuccess(int i, Header[] headers, File file) {
				if (file == null || !file.exists()) {
					LogUtil.e("lyt", "FileHttpHandler onSuccess file等于null!");
					return;
				}
				//解压文件
				boolean israr=FileUtil.unRarFile(file.getAbsolutePath(),FileUtil.gethtmlpath()+"/h5");
				sp.edit().putInt(spname,code).commit();
				if(israr){
					file.delete();
				}
			}

			@Override
			public void onFailure(int i, Header[] headers, Throwable throwable, File file) {
				LogUtil.e("lyt", "文件下载失败！！！"+throwable.toString());
				deleteFile();//删除文件
			}
			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				super.onProgress(bytesWritten, totalSize);
			}
			private void deleteFile() {
				if (file.exists()) {
					file.delete();
				}
			}
			@Override
			public void onFinish() {
				LogUtil.e("lyt",spname+"文件下载结束");
				super.onFinish();
			}
			@Override
			public void onCancel() {
				super.onCancel();
				deleteFile();
			}
		}

	}