package com.meiyin.erp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiyin.erp.entity.Setting_Entity;
import com.meiyin.erp.R;

public class Setting_Adapter extends Adapter<Setting_Entity>{
	private Context mContext;
	private LayoutInflater inflater;
	
	public Setting_Adapter(Context mContext) {
		super(mContext);
		this.inflater = LayoutInflater.from(mContext);
		// TODO Auto-generated constructor stub
	}
	public static class ViewHolder {
		ImageView pic;//环节名称
		TextView name;//处理信息

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.setting_dialog, null);
			holder = new ViewHolder();
			holder.pic = (ImageView) convertView
					.findViewById(R.id.pic);
			holder.name = (TextView) convertView
					.findViewById(R.id.names);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Setting_Entity lists = mList.get(position);
		
		Drawable pic = lists.getPic();
		String Name=lists.getName();
		


		holder.pic.setImageDrawable(pic);//图片
		holder.name.setText(Name);//名字
		
		return convertView;
	}
}
