package com.meiyin.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meiyin.erp.entity.AddMenuEntity;
import com.meiyin.erp.R;

public class AddMenu_Adapter extends Adapter<AddMenuEntity>{
	protected Context mContext;
	private LayoutInflater inflater;
	
	public AddMenu_Adapter(Context mContext) {
		super(mContext);
		this.mContext=mContext;
		this.inflater = LayoutInflater.from(mContext);
		// TODO Auto-generated constructor stub
	}
	public static class ViewHolder {
		TextView addmenu_textview;//名称
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.add_gridview_item, null);
			holder = new ViewHolder();
			holder.addmenu_textview = (TextView) convertView
					.findViewById(R.id.addmenu_textview);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AddMenuEntity lists = mList.get(position);
		String Code_desc = lists.getCode_desc();
		holder.addmenu_textview.setText(Code_desc);//事件处理信息
		return convertView;
	}
}