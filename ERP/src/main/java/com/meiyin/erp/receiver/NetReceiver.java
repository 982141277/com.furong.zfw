package com.meiyin.erp.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.meiyin.erp.util.NetUtils;

/**
 * @author liuyuting
 * @version 2015年9月12日 上午09:34:52
 */
public class NetReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
			boolean isConnected = NetUtils.isNetworkConnected(context);
	        System.out.println("网络状态：" + isConnected);
	        System.out.println("wifi状态：" + NetUtils.isWifiConnected(context));
	        System.out.println("移动网络状态：" + NetUtils.isMobileConnected(context));
	        System.out.println("网络连接类型：" + NetUtils.getConnectedType(context));
	        if (isConnected) {
	        	Toast.makeText(context, "已经连接网络", Toast.LENGTH_LONG).show();
	        } else {
	        	Toast.makeText(context, "网络连接已中断", Toast.LENGTH_LONG).show();
	        }
		}
	}

}

