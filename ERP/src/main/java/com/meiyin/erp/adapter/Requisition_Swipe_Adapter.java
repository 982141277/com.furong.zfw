package com.meiyin.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.meiyin.erp.entity.Requisition_Swipe_Entity;
import com.meiyin.erp.R;


public class Requisition_Swipe_Adapter extends Adapter<Requisition_Swipe_Entity>{
	private Context mContext;
	int mRightWidth;
	private LayoutInflater inflater;

	public Requisition_Swipe_Adapter(Context mContext,int mRightWidth) {
		super(mContext);
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);
		this.mRightWidth=mRightWidth;
	}

	public static class ViewHolder {
		protected TextView swipe_prod_name_0;//名称
		protected TextView swipe_need_num;//数量
		protected TextView swipe_prod_Model;//型号
		protected TextView swipe_unit_price;//单价
		protected TextView swipe_url;//网店参考地址
		protected TextView swipe_remark;//备注
		
		protected LinearLayout item_right_txt1;
		protected LinearLayout item_right_txt2;
		protected LinearLayout item_right;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.swipelist_item, null);
			holder = new ViewHolder();
			holder.swipe_prod_name_0 = (TextView) convertView
					.findViewById(R.id.swipe_prod_name_0);//名称
			holder.swipe_need_num = (TextView) convertView
					.findViewById(R.id.swipe_need_num);//数量
			holder.swipe_prod_Model = (TextView) convertView
					.findViewById(R.id.swipe_prod_Model);//型号
			holder.swipe_unit_price = (TextView) convertView
					.findViewById(R.id.swipe_unit_price);//单价
			holder.swipe_url = (TextView) convertView
					.findViewById(R.id.swipe_url);//参考地址
			holder.swipe_remark = (TextView) convertView
					.findViewById(R.id.swipe_remark);//备注
			
			
			
			holder.item_right = (LinearLayout) convertView
					.findViewById(R.id.myitem_right);
			holder.item_right_txt1 = (LinearLayout) convertView
					.findViewById(R.id.myitem_right_txt1);
			holder.item_right_txt2 = (LinearLayout) convertView
					.findViewById(R.id.myitem_right_txt2);
			
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.item_right_txt1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onRightItemClick(v, position);
				}
			}
		});
		holder.item_right_txt2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListenerLeft != null) {
					mListenerLeft.onLeftItemClick(v, position);
				}
			}
		});
		LayoutParams lp2 = new LayoutParams(mRightWidth,
				LayoutParams.MATCH_PARENT);
		holder.item_right.setLayoutParams(lp2);
		Requisition_Swipe_Entity list = mList.get(position);
		String Org_name = list.getOrg_name();
		String Accompany_name=list.getAccompany_name();
		String AccompanyId2_name = list.getAccompanyId2_name();
		String Need_num=list.getNeed_num();
		String Prod_Model = list.getProd_Model();
		String Prod_unit=list.getProd_unit();
		String Unit_price=list.getUnit_price();
		String Urls = list.getUrl();
		String Remark=list.getRemark();
	
		holder.swipe_prod_name_0.setText(Org_name+"/"+Accompany_name+"/"+AccompanyId2_name);
		holder.swipe_need_num.setText("数量:"+Need_num+Prod_unit);
		holder.swipe_prod_Model.setText("型号:"+Prod_Model);
		holder.swipe_unit_price.setText("单价:"+Unit_price);
		
		if(Urls.equals("")){
			holder.swipe_url.setVisibility(ViewGroup.GONE);
		}else {
			holder.swipe_url.setVisibility(ViewGroup.VISIBLE);
		}
		if(Remark.equals("")){
			holder.swipe_remark.setVisibility(ViewGroup.GONE);
		}else{
			holder.swipe_remark.setVisibility(ViewGroup.VISIBLE);
		}
		
		holder.swipe_url.setText(Urls);
		holder.swipe_remark.setText("备注:"+Remark);
		
		return convertView;
	
	}

	/**
	 * 单击事件监听器
	 */
	public interface onRightItemClickListener {
		void onRightItemClick(View v, int position);
	}
	public interface onLeftItemClickListener {
		void onLeftItemClick(View v, int position);
	}
	private onRightItemClickListener mListener = null;
	private onLeftItemClickListener mListenerLeft = null;

	public void setOnRightItemClickListener(onRightItemClickListener listener) {
		mListener = listener;
	}

	public void setOnLeftItemClickListener(onLeftItemClickListener listener) {
		mListenerLeft = listener;
	}
}
