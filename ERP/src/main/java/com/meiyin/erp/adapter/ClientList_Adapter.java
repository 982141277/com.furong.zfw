package com.meiyin.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.meiyin.erp.entity.SellClientInfoEntity;
import com.meiyin.erp.R;

public class ClientList_Adapter extends Adapter<SellClientInfoEntity>{
	private Context mContext;
	private LayoutInflater inflater;
	
	public ClientList_Adapter(Context mContext) {
		// TODO Auto-generated constructor stub
		super(mContext);
		this.mContext=mContext;
		this.inflater = LayoutInflater.from(mContext);
	}

	public static class ViewHolder {
		CheckBox client_checkbox;//选择项
		TextView client_name;//环节名称
		TextView client_WorkAddr;//客户地址
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.client_list_item, null);
			holder = new ViewHolder();
			holder.client_checkbox = (CheckBox) convertView
			.findViewById(R.id.client_checkbox);
			holder.client_name = (TextView) convertView
					.findViewById(R.id.client_name);
			holder.client_WorkAddr = (TextView) convertView
					.findViewById(R.id.client_WorkAddr);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		SellClientInfoEntity lists = mList.get(position);		
		holder.client_checkbox.setOnCheckedChangeListener(new CheckedChangeListener(holder.client_checkbox,position));
		holder.client_checkbox.setChecked(lists.getIsChecked());

		String Name = lists.getsClientName();
		String WorkAddr=lists.getsClientWorkAddr();
		
		holder.client_name.setText("客户名称："+Name);//客户名称
		holder.client_WorkAddr.setText("客户地址："+WorkAddr);//地址

		return convertView;
		
	}
	private class  CheckedChangeListener implements CompoundButton.OnCheckedChangeListener{
		private int position;
		private CheckBox client_checkbox;

		private CheckedChangeListener(CheckBox client_checkbox,int position){
			this.position=position;
			this.client_checkbox=client_checkbox;
		}
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			client_checkbox.setChecked(arg1);
			mList.get(position).setIsChecked(arg1);
		}
	}
}
