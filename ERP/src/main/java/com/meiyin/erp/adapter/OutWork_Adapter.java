package com.meiyin.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meiyin.erp.entity.OutWork_Entity;
import com.meiyin.erp.R;
import com.meiyin.erp.util.DateUtil;

public class OutWork_Adapter extends Adapter<OutWork_Entity>{
	private Context mContext;
	private LayoutInflater inflater;
	
	public OutWork_Adapter(Context mContext) {
		super(mContext);
		this.mContext=mContext;
		this.inflater = LayoutInflater.from(mContext);
		// TODO Auto-generated constructor stub
	}
	public static class ViewHolder {
		TextView outtype_text;//外出类型
		TextView Applicant_text;//申请人
		TextView Section_text;//所属部门
		TextView Time_text;//时间
		TextView isBrushString;//是否完成打卡
		TextView text_mainContent;//内容
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.outwork_item, null);
			holder = new ViewHolder();
			holder.outtype_text = (TextView) convertView
					.findViewById(R.id.outtype_text);
			holder.Applicant_text = (TextView) convertView
					.findViewById(R.id.Applicant_text);
			holder.Section_text = (TextView) convertView
					.findViewById(R.id.Section_text);
			holder.Time_text = (TextView) convertView
					.findViewById(R.id.Time_text);
			holder.isBrushString = (TextView) convertView
					.findViewById(R.id.isBrushString);
			holder.text_mainContent = (TextView) convertView
					.findViewById(R.id.text_mainContent);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		OutWork_Entity lists = mList.get(position);
		
		String OutType = lists.getOuttype();
		String Applicant=lists.getApplicant();
		String Section=lists.getSection();
		Long Time=lists.getTime();
		String IsBrushString=lists.getBrushstring();
		String MainContent=lists.getMainContent();
		String ToLocation=lists.getToLocation();
		
		
		holder.text_mainContent.setText(MainContent+"    ("+ToLocation+")");
		holder.isBrushString.setText(IsBrushString);
		holder.outtype_text.setText(OutType);//外出类型
		holder.Applicant_text.setText(Applicant);//申请人
		holder.Section_text.setText(Section);//所属部门
		holder.Time_text.setText(DateUtil.convertLongToDate(Time));//创建时间
		if(IsBrushString.equals("1")){
			holder.isBrushString.setText("已刷卡");
			holder.isBrushString.setTextColor(mContext.getResources().getColor(R.color.text_green));
		}else if(IsBrushString.equals("0")){
			holder.isBrushString.setText("未刷卡");
			holder.isBrushString.setTextColor(mContext.getResources().getColor(R.color.dark_red));
		}else if(IsBrushString.equals("2")){
			holder.isBrushString.setText("刷卡异常");
			holder.isBrushString.setTextColor(mContext.getResources().getColor(R.color.dark_red));
		}		
		
		return convertView;
	}
}