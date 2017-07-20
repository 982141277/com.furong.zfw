package com.meiyin.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meiyin.erp.entity.Gooutbrush_Entity;
import com.meiyin.erp.R;

public class GooutBrush_Adapter extends Adapter<Gooutbrush_Entity>{
	protected Context mContext;
	private LayoutInflater inflater;
	
	public GooutBrush_Adapter(Context mContext) {
		super(mContext);
		this.mContext=mContext;
		this.inflater = LayoutInflater.from(mContext);
		// TODO Auto-generated constructor stub
	}
	public static class ViewHolder {
		TextView goout_brush_address;//地址
		TextView goout_brush_time;//时间
		TextView goout_brush_describe;//事由
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.gout_brush_item, null);
			holder = new ViewHolder();
			holder.goout_brush_address = (TextView) convertView
					.findViewById(R.id.goout_brush_address);
			holder.goout_brush_time = (TextView) convertView
					.findViewById(R.id.goout_brush_time);
			holder.goout_brush_describe = (TextView) convertView
					.findViewById(R.id.goout_brush_describe);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Gooutbrush_Entity lists = mList.get(position);
		String Brushaddress = lists.getBrushaddress();
		String Brushtime = lists.getBrushtime();
		String Describe = lists.getDescribes();
		holder.goout_brush_address.setText(Brushaddress);//地址
		holder.goout_brush_time.setText(Brushtime);//时间
		holder.goout_brush_describe.setText(Describe);//事由
		return convertView;
	}
}
