package com.meiyin.erp.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiyin.erp.entity.IT_Event_SQ_Entity;
import com.meiyin.erp.R;
import com.meiyin.erp.util.DateUtil;
import com.meiyin.erp.util.Function;



public class IT_History_Adapter extends Adapter<IT_Event_SQ_Entity> {

	private Context mContext;
	private LayoutInflater inflater;
	
	public IT_History_Adapter(Context mContext) {
		super(mContext);
		this.mContext=mContext;
		this.inflater = LayoutInflater.from(mContext);
		// TODO Auto-generated constructor stub
	}
	public static class ViewHolder {
		TextView state;//环节名称
		TextView clxx;//处理信息
		TextView name;//名字
		TextView time;//时间
		LinearLayout linear_clxx;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.it_history_item, null);
			holder = new ViewHolder();
			holder.state = (TextView) convertView
					.findViewById(R.id.state_name);
			holder.clxx = (TextView) convertView
					.findViewById(R.id.clxx);
			holder.name = (TextView) convertView
					.findViewById(R.id.name);
			holder.time = (TextView) convertView
					.findViewById(R.id.time);
			holder.linear_clxx = (LinearLayout) convertView
					.findViewById(R.id.linear_clxx);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		IT_Event_SQ_Entity lists = mList.get(position);
		
		String time = lists.getOper_time();
		String clxx=lists.getContent();
		String name = lists.getUsername();
		String state = lists.getState();


		String ent = time.substring(5,
				10);
		Long ts = DateUtil.getLongFromStr(time);
		String dates = DateUtil.getHourTimeStr(ts).substring(0, 5);
		String S = DateUtil.convertLong00(System.currentTimeMillis());
		Long h = DateUtil.getLongFromStr(S);
		long t = h - ts;
		long times = t / 1000 / 3600;
		if (times > 24 && times < 48) {
			holder.time.setText("前日 " + dates);//事件时间
		} else if (times < 24 && times > 0) {
			holder.time.setText("昨日 " + dates);//事件时间
		} else if (times < 0) {
			holder.time.setText("今日 " + dates);//事件时间
		} else if (times > 48) {
			holder.time.setText(ent + " " + dates);//事件时间
		}

		holder.name.setText(name);//名字

		if(state.equals("0")){
			holder.state.setText(Html.fromHtml(clxx, Function.imgGetter, null));//事件处理信息
			holder.name.setVisibility(ViewGroup.VISIBLE);
			holder.linear_clxx.setVisibility(ViewGroup.GONE);
			holder.state.setTextColor(mContext.getResources().getColor(R.color.text_graya));
		}else if(state.equals("1")){
			holder.state.setText(Html.fromHtml(clxx, Function.imgGetter, null));//事件处理信息
			holder.name.setVisibility(ViewGroup.VISIBLE);
			holder.linear_clxx.setVisibility(ViewGroup.GONE);
			holder.state.setTextColor(mContext.getResources().getColor(R.color.text_graya));
		}else if(state.equals("2")){
			holder.state.setText(Html.fromHtml(clxx, Function.imgGetter, null));//事件处理信息
			holder.name.setVisibility(ViewGroup.GONE);
			holder.linear_clxx.setVisibility(ViewGroup.GONE);
			holder.state.setTextColor(mContext.getResources().getColor(R.color.text_graya));			
		}else if(state.equals("3")){
			holder.state.setText(Html.fromHtml(clxx, Function.imgGetter, null));//事件处理信息
			holder.name.setVisibility(ViewGroup.GONE);
			holder.linear_clxx.setVisibility(ViewGroup.GONE);
			holder.state.setTextColor(mContext.getResources().getColor(R.color.text_graya));			
		}else if(state.equals("4")){
			holder.name.setVisibility(ViewGroup.GONE);
			holder.linear_clxx.setVisibility(ViewGroup.GONE);
			holder.state.setText(Html.fromHtml(clxx, Function.imgGetter, null));//事件处理信息
			holder.state.setTextColor(mContext.getResources().getColor(R.color.text_graya));			
		}else if(state.equals("5")){
			holder.name.setVisibility(ViewGroup.GONE);
			holder.linear_clxx.setVisibility(ViewGroup.GONE);
			holder.state.setText(Html.fromHtml(clxx, Function.imgGetter, null));//事件处理信息
			holder.state.setTextColor(mContext.getResources().getColor(R.color.text_graya));			
		}else if(state.equals("6")){
			holder.state.setText("已解决");
			holder.name.setVisibility(ViewGroup.VISIBLE);
			holder.linear_clxx.setVisibility(ViewGroup.VISIBLE);
			holder.clxx.setText(Html.fromHtml(clxx, Function.imgGetter, null));//事件处理信息
			holder.state.setTextColor(mContext.getResources().getColor(R.color.text_green));
		}else if(state.equals("7")){
			holder.state.setText("满意度调查");
			holder.name.setVisibility(ViewGroup.VISIBLE);
			holder.clxx.setVisibility(ViewGroup.VISIBLE);
			holder.linear_clxx.setVisibility(ViewGroup.VISIBLE);
			holder.clxx.setText(Html.fromHtml(clxx, Function.imgGetter, null));//事件处理信息
			holder.state.setTextColor(mContext.getResources().getColor(R.color.text_green));
		}else if(state.equals("8")){
			holder.state.setText("8");
		}else{
			holder.state.setText("异常666");

		}

		return convertView;
	}
}
