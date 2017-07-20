package com.meiyin.erp.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meiyin.erp.R;
import com.meiyin.erp.entity.Memu_History;
import com.meiyin.erp.util.Function;

public class Memu_History_Adapter extends Adapter<Memu_History>{

	private Context mContext;
	private LayoutInflater inflater;
	public Memu_History_Adapter(Context mContext) {
		super(mContext);
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);
		// TODO Auto-generated constructor stub
	}
	static class ViewHolder {
		TextView shenqingren;
		TextView zhuangtai;
		TextView shijian;
		TextView shenpiren;
		TextView suoshubumen;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.memu_history_model, null);
			holder = new ViewHolder();
			holder.shenqingren = (TextView) convertView
					.findViewById(R.id.shenqingren);
			holder.zhuangtai = (TextView) convertView
					.findViewById(R.id.zhuangtai);
			holder.shijian = (TextView) convertView
					.findViewById(R.id.shijian);
			holder.shenpiren = (TextView) convertView
					.findViewById(R.id.shenpiren);
			holder.suoshubumen = (TextView) convertView
					.findViewById(R.id.suoshubumen);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Memu_History memulists = mList.get(position);
		String AppUser = memulists.getAppUser();
		String App_state=memulists.getApp_state();
		String App_date = memulists.getApp_date();
		String Dept=memulists.getDept();
		String App_view = memulists.getApp_view();

		if(null!=App_state){
		if(App_state.equals("0")){
		holder.zhuangtai.setText("未处理");
		holder.zhuangtai.setTextColor(mContext.getResources().getColor(R.color.dark_red));
		}else if(App_state.equals("3")){
			holder.zhuangtai.setText("批准");
			holder.zhuangtai.setTextColor(mContext.getResources().getColor(R.color.text_green));
		}else if(App_state.equals("7")){
			holder.zhuangtai.setText("作废");
			holder.zhuangtai.setTextColor(mContext.getResources().getColor(R.color.dark_red));
		}else if(App_state.equals("8")){
			holder.zhuangtai.setText("已提交");
		}else if(App_state.equals("9")){
			holder.zhuangtai.setText("已填写详情");
			holder.zhuangtai.setTextColor(mContext.getResources().getColor(R.color.text_green));
		}else if(App_state.equals("5")){
			holder.zhuangtai.setText("核实通过");
			holder.zhuangtai.setTextColor(mContext.getResources().getColor(R.color.text_green));
		}else if(App_state.equals("57")){
			holder.zhuangtai.setText("无考勤");
			holder.zhuangtai.setTextColor(mContext.getResources().getColor(R.color.dark_red));
		}
		}
		holder.shenqingren.setText(AppUser);
		if(App_date.equals("0")){
			holder.shijian.setText("");
		}else{
//		holder.shijian.setText(DateUtil.getEnglishTime(App_date));
		holder.shijian.setText(App_date);
		}
		holder.suoshubumen.setText(Dept);
		if(App_view==null){
			App_view="";
		}
		holder.shenpiren.setText(Html.fromHtml(App_view, Function.imgGetter, null));

		return convertView;
	}
	
}
