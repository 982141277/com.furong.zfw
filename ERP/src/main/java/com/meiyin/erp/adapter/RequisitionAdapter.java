package com.meiyin.erp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meiyin.erp.R;
import com.meiyin.erp.activity.RequisitionDetailActivity;
import com.meiyin.erp.entity.Requisition_Entity;

public class RequisitionAdapter extends Adapter<Requisition_Entity>{
	private RequisitionDetailActivity mContext;
	private LayoutInflater inflater;
	
	public RequisitionAdapter(RequisitionDetailActivity mContext) {
		super(mContext);
		this.mContext=mContext;
		this.inflater = LayoutInflater.from(mContext);
	}
	
	public static class ViewHolder {
		private TextView prod_name_0;//物品名称
		private TextView need_num;//需要数量
		private TextView prod_Model;//产品型号
		private TextView unit_price;//参考单价
		private TextView url;//网店参考链接
		private TextView remark;//备注

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.requisition_item, null);
			holder = new ViewHolder();
			holder.prod_name_0 = (TextView) convertView
					.findViewById(R.id.requisition_prod_name_0);//物品名称
			holder.need_num = (TextView) convertView
					.findViewById(R.id.requisition_need_num);//需要数量
			holder.prod_Model = (TextView) convertView
					.findViewById(R.id.requisition_prod_Model);//产品型号
			holder.unit_price = (TextView) convertView
					.findViewById(R.id.requisition_unit_price);//参考单价
			holder.url = (TextView) convertView
					.findViewById(R.id.requisition_url);//网店参考链接
			holder.remark = (TextView) convertView
					.findViewById(R.id.requisition_remark);//备注
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Requisition_Entity lists = mList.get(position);
		
		String Prod_unit = lists.getProd_unit();//单位

		String Prod_name = lists.getProd_name_0();//物品名称
		String Need_num=lists.getNeed_num();//需要数量
		String Prod_Model=lists.getProd_Model();//产品型号
		String Unit_price=lists.getUnit_price();//参考单价
		final String Url=lists.getUrl();//网店参考链接
		String Remark=lists.getRemark();//备注
		holder.prod_name_0.setText(Prod_name);//物品名称
		holder.need_num.setText("数量："+Need_num+Prod_unit);//需要数量
		holder.prod_Model.setText("型号："+Prod_Model);//产品型号
		holder.unit_price.setText("单价：￥"+Unit_price);//参考单价
		if(Url.equals("")){
			holder.url.setVisibility(ViewGroup.GONE);
		}
		holder.url.setText(Url);//网店参考链接
		
		if(Remark.equals("")){
			holder.remark.setVisibility(ViewGroup.GONE);
		}
		holder.remark.setText("备注："+Remark);////备注
		
		return convertView;
	}
}
