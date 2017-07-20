package com.meiyin.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meiyin.erp.entity.IT_Management_Event_Entity;
import com.meiyin.erp.R;
import com.meiyin.erp.activity.IT_Management_Event_List;
import com.meiyin.erp.util.DateUtil;

public class IT_Management_Adapter extends Adapter<IT_Management_Event_Entity>{

	private Context mContext;
	private IT_Management_Event_List list;
	private LayoutInflater inflater;

	public IT_Management_Adapter(Context mContext, IT_Management_Event_List list) {
		super(mContext);
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list=list;
		this.inflater = LayoutInflater.from(mContext);
	}

	public static class ViewHolder {
		TextView event_no;//事件编号
		TextView title;//事件标题
//		TextView source;//事件来源
		TextView build_time;//新建事件时间
		TextView state;//事件状态
		TextView event_level;//优先级
		
/*		TextView sj_type;
		TextView bg_address;
		TextView lx_phone;
		TextView chulishichang;
		TextView chuliren;*/
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.it_management_event_model, null);
			holder = new ViewHolder();
			holder.event_no = (TextView) convertView
					.findViewById(R.id.sj_bianhao);
			holder.title = (TextView) convertView
					.findViewById(R.id.sj_biaoti_);
/*			holder.source = (TextView) convertView
					.findViewById(R.id.sj_laiyuan);*/
			holder.event_level = (TextView) convertView
					.findViewById(R.id.youxianji);
			holder.build_time = (TextView) convertView
					.findViewById(R.id.xj_time);
			holder.state = (TextView) convertView
					.findViewById(R.id.sj_zhuangtai);
			
/*			holder.sj_type = (TextView) convertView
					.findViewById(R.id.sj_type);
			holder.bg_address = (TextView) convertView
					.findViewById(R.id.bg_address);
			holder.lx_phone = (TextView) convertView
					.findViewById(R.id.lx_phone);
			holder.chuliren = (TextView) convertView
					.findViewById(R.id.chuliren);
			holder.chulishichang = (TextView) convertView
					.findViewById(R.id.chulishichang);*/
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		IT_Management_Event_Entity lists = mList.get(position);
		String event_no = lists.getEvent_no();
		String title=lists.getTitle();
		String source = lists.getSource();
		String build_time = lists.getBuild_time();
		String state=lists.getState();
		String event_level = lists.getEvent_level();
		
/*		String sj_type=lists.getSj_type();
		String bg_address = lists.getBg_address();
		String lx_phone=lists.getLx_phone();
		String chuliren = lists.getChuliren();
		String chulishichang = lists.getChulishichang();*/

		String ent = build_time.substring(5,
				10);
		Long ts = DateUtil.getLongFromStr(build_time);
		String dates = DateUtil.getHourTimeStr(ts).substring(0, 5);
		String S = DateUtil.convertLong00(System.currentTimeMillis());
		Long h = DateUtil.getLongFromStr(S);
		long t = h - ts;
		long times = t / 1000 / 3600;
		if (times > 24 && times < 48) {
			holder.build_time.setText("前日 " + dates);//新建事件时间
		} else if (times < 24 && times > 0) {
			holder.build_time.setText("昨日 " + dates);//新建事件时间
		} else if (times < 0) {
			holder.build_time.setText("今日 " + dates);//新建事件时间
		} else if (times > 48) {
			holder.build_time.setText(ent + " " + dates);//新建事件时间
		}
		
		holder.event_no.setText(event_no);//事件编号
		holder.title.setText(title+" ("+source+")");//事件标题
//		holder.source.setText(source);//事件来源	
		holder.state.setText(state);//事件状态
		holder.event_level.setText(event_level);//优先级
		if(event_level.equals("紧急")){
			holder.event_level.setTextColor(mContext.getResources().getColor(R.color._red));
		}else if(event_level.equals("高")){
			holder.event_level.setTextColor(mContext.getResources().getColor(R.color._red));
		}else if (event_level.equals("中")) {
			holder.event_level.setTextColor(mContext.getResources().getColor(R.color.text_graya));
		}else{
			holder.event_level.setTextColor(mContext.getResources().getColor(R.color.text_grays));
		}
		
		
		if(state.equals("未处理")){
			holder.state.setTextColor(mContext.getResources().getColor(R.color.text_graya));
		}else if(state.equals("正处理")){
			holder.state.setTextColor(mContext.getResources().getColor(R.color.text_graya));			
		}else if(state.equals("挂起")){
			holder.state.setTextColor(mContext.getResources().getColor(R.color.text_graya));			
		}else if(state.equals("已解决")){
			holder.state.setTextColor(mContext.getResources().getColor(R.color.text_graya));			
		}else if(state.equals("未分配")){
			holder.state.setTextColor(mContext.getResources().getColor(R.color.text_graya));			
		}else if(state.equals("满意度调查")){
			holder.state.setTextColor(mContext.getResources().getColor(R.color.text_graya));			
		}else{
			holder.state.setTextColor(mContext.getResources().getColor(R.color.text_graya));
		}
		
/*		holder.sj_type.setText(sj_type);
		holder.bg_address.setText(bg_address);
		holder.lx_phone.setText(lx_phone);
		holder.chuliren.setText(chuliren);
		holder.chulishichang.setText(chulishichang);*/
		return convertView;
	}
}
