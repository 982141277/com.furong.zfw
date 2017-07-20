package com.meiyin.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meiyin.erp.R;
import com.meiyin.erp.entity.MyTaskEntity;

public class MyTaskAdapter extends Adapter<MyTaskEntity> {
	private Context mContext;
	private LayoutInflater inflater;

	public MyTaskAdapter(Context mContext) {
		super(mContext);
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);
		// TODO Auto-generated constructor stub
	}

	public static class ViewHolder {
		public TextView mytask_item_week;// 星期
		public TextView mytask_item_time;// 时间
		public TextView is_write_task;
		public TextView ischecks;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.mytask_list_item, null);
			holder = new ViewHolder();
			holder.mytask_item_week = (TextView) convertView
					.findViewById(R.id.mytask_item_week);
			holder.mytask_item_time = (TextView) convertView
					.findViewById(R.id.mytask_item_time);
			holder.is_write_task = (TextView) convertView
					.findViewById(R.id.is_write_task);
			holder.ischecks = (TextView) convertView
					.findViewById(R.id.ischecks);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MyTaskEntity lists = mList.get(position);
		String week = lists.getWeekd();
		String date = lists.getWork_date();
		String staffDayReport = lists.getStaffDayReport();
		String ischecks = lists.getIscheck();
		if(ischecks.equals("1")){
		holder.ischecks.setText("x");
		holder.ischecks.setTextColor(mContext.getResources().getColor(R.color._red));
		}else{
			holder.ischecks.setText("√");
			holder.ischecks.setTextColor(mContext.getResources().getColor(R.color._green));
		}
		if (staffDayReport.equals("edit")) {
			holder.is_write_task.setVisibility(ViewGroup.VISIBLE);
		} else {
			holder.is_write_task.setVisibility(ViewGroup.GONE);
		}
		holder.mytask_item_week.setText(week);// 星期
		holder.mytask_item_time.setText(date);// 工作时间
		holder.is_write_task.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mListener != null) {
					mListener.onRightItemClick(arg0, position);
				}
			}
		});
		
		return convertView;
	}
	private onRightItemClickListener mListener = null;
	
	public void setOnRightItemClickListener(onRightItemClickListener listener) {
		mListener = listener;
	}
	public interface onRightItemClickListener {
		void onRightItemClick(View v, int position);
	}
}
