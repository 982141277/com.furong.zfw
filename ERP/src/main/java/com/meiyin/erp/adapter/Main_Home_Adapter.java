package com.meiyin.erp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiyin.erp.R;
import com.meiyin.erp.entity.Get_Logins;



public class Main_Home_Adapter extends Adapter<Get_Logins>{

	private Context mContext;
	private LayoutInflater inflater;
	Intent intent;
	public Main_Home_Adapter(Context mContext) {
		super(mContext);
		// TODO Auto-generated constructor stub
		intent = new Intent();
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);
	}

	static class ViewHolder {
		TextView text_one;
		TextView hongdiandian;
		RelativeLayout relativelaouts;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.main_home_model, null);
			holder = new ViewHolder();
			holder.text_one = (TextView) convertView
					.findViewById(R.id.text_one);
			holder.hongdiandian = (TextView) convertView
					.findViewById(R.id.hongdiandian);
			holder.relativelaouts = (RelativeLayout) convertView
					.findViewById(R.id.relativelaouts);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Get_Logins list = mList.get(position);
		String name = list.getSystem_name();
		Long sz = list.getSize();
		if(sz==null||sz<1){
		holder.relativelaouts.setVisibility(ViewGroup.GONE);
		}else{
			holder.relativelaouts.setVisibility(ViewGroup.VISIBLE);
			holder.hongdiandian.setText(""+sz);
		}
		holder.text_one.setText(name);
		return convertView;
	
	}
}
