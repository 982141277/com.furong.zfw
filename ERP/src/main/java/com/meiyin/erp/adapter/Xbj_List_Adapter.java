package com.meiyin.erp.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meiyin.erp.entity.Xjb_List_Entity;
import com.meiyin.erp.R;

public class Xbj_List_Adapter extends Adapter<Xjb_List_Entity>{
	protected Context mContext;
	private LayoutInflater inflater;
	
	public Xbj_List_Adapter(Context mContext) {
		super(mContext);
		this.mContext=mContext;
		this.inflater = LayoutInflater.from(mContext);
		// TODO Auto-generated constructor stub
	}
	public static class ViewHolder {
		TextView xbj_list_name;//名称
		TextView xbj_list_names;//巡检人
		TextView xbj_time1;//应巡检时间
		TextView xbj_time2;//实际巡检时间
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.xbj_list_item, null);
			holder = new ViewHolder();
			holder.xbj_list_name = (TextView) convertView
					.findViewById(R.id.xbj_list_name);
			holder.xbj_list_names = (TextView) convertView
					.findViewById(R.id.xbj_list_names);
			holder.xbj_time1 = (TextView) convertView
					.findViewById(R.id.xbj_time1);
			holder.xbj_time2 = (TextView) convertView
					.findViewById(R.id.xbj_time2);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Xjb_List_Entity lists = mList.get(position);
		String Area_name = lists.getArea_name();
		String Exam_man_name = lists.getExam_man_name();
		String Pro_exam_time = lists.getPro_exam_time();
		String Real_exam_time = lists.getReal_exam_time();
		String MarkRed = lists.getMarkRed();
		if(null==Real_exam_time){
			Real_exam_time="";
		}
		holder.xbj_list_name.setText((position+1)+"、"+Html.fromHtml(Area_name));//事件处理信息
		holder.xbj_list_names.setText(Exam_man_name);//巡检人
		holder.xbj_time1.setText(Pro_exam_time);//应巡检时间
		holder.xbj_time2.setText(Real_exam_time);//实际巡检时间
		if(MarkRed.equals("1")){
			holder.xbj_time2.setTextColor(mContext.getResources().getColor(R.color._red));
		}
		
		return convertView;
	}
}
