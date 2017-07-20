package com.meiyin.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.meiyin.erp.R;
import com.meiyin.erp.entity.MyPage_Entity;

public class AddShortCut_Adapter extends Adapter<MyPage_Entity>{
	private Context mContext;
	private LayoutInflater inflater;

	public AddShortCut_Adapter(Context mContext) {
		super(mContext);
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);
		// TODO Auto-generated constructor stub
	}

	public static class ViewHolder {
		public TextView add_shortcut_item_name;// 名称
		public CheckBox add_shortcut_item_box;//选取状态
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.add_short_cut_item, null);
			holder = new ViewHolder();
			holder.add_shortcut_item_name = (TextView) convertView
					.findViewById(R.id.add_shortcut_item_name);
			holder.add_shortcut_item_box = (CheckBox) convertView
					.findViewById(R.id.add_shortcut_item_box);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MyPage_Entity list = mList.get(position);
		String xName = list.getxName();
		Boolean xSelect = list.getxSelect();
		holder.add_shortcut_item_name.setText(xName);//名称 
		holder.add_shortcut_item_box.setChecked(xSelect);

		
		return convertView;
	}
}
