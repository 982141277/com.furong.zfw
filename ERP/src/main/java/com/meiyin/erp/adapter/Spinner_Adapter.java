package com.meiyin.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meiyin.erp.entity.Spinner_Entity;
import com.meiyin.erp.R;

public class Spinner_Adapter extends Adapter<Spinner_Entity>{
	private Context mContext;
	private LayoutInflater inflater;
	
	public Spinner_Adapter(Context mContext) {
		super(mContext);
		this.mContext=mContext;
		this.inflater = LayoutInflater.from(mContext);
		// TODO Auto-generated constructor stub
	}
	public static class ViewHolder {
		TextView names;//名称
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.spinner_items, null);
			holder = new ViewHolder();
			holder.names = (TextView) convertView
					.findViewById(R.id.names);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Spinner_Entity spinner_Entity = mList.get(position);
		String name = spinner_Entity.getType_name();
		holder.names.setText(name);//标题
		return convertView;
	}
}
