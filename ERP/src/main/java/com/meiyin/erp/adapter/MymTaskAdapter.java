package com.meiyin.erp.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meiyin.erp.R;
import com.meiyin.erp.entity.MymTaskEntity;

public class MymTaskAdapter extends Adapter<MymTaskEntity> {
	private Context mContext;
	private LayoutInflater inflater;
	private String Stafforg;

	public MymTaskAdapter(Context mContext, String Stafforg) {
		super(mContext);
		this.mContext = mContext;
		this.Stafforg = Stafforg;
		this.inflater = LayoutInflater.from(mContext);
		// TODO Auto-generated constructor stub
	}

	public static class ViewHolder {
		private TextView mymtask_item_title;// 标题
		private TextView mymtask_item_content;// 内容
		private TextView mymtask_item_time;// 汇报时间
		private TextView mymtask_item_isfinish;// 是否完成
		private TextView mytask_report;;// 汇报情况
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.mymtask_list_item, null);
			holder = new ViewHolder();
			holder.mymtask_item_title = (TextView) convertView
					.findViewById(R.id.mymtask_item_title);
			holder.mymtask_item_content = (TextView) convertView
					.findViewById(R.id.mymtask_item_content_s);
			holder.mytask_report = (TextView) convertView
					.findViewById(R.id.mytask_report);
			holder.mymtask_item_time = (TextView) convertView
					.findViewById(R.id.mymtask_item_time);
			holder.mymtask_item_isfinish = (TextView) convertView
					.findViewById(R.id.mymtask_item_isfinish);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MymTaskEntity lists = mList.get(position);
		String name = lists.getName();
		String Notes = lists.getNotes();
		String Time = lists.getTime();
		String status = lists.getStatus();
		if (null != status) {

			if (status.equals("0")) {
				holder.mymtask_item_isfinish.setText("未完成");
				holder.mymtask_item_isfinish.setTextColor(mContext
						.getResources().getColor(R.color.dark_red));
				if (Stafforg.equals("edit")) {
					holder.mytask_report.setVisibility(ViewGroup.VISIBLE);
				} else {
					holder.mytask_report.setVisibility(ViewGroup.GONE);
				}
				holder.mymtask_item_content.setVisibility(ViewGroup.GONE);
			} else {
				holder.mymtask_item_isfinish.setText("已完成");
				holder.mymtask_item_isfinish.setTextColor(mContext
						.getResources().getColor(R.color.text_green));
				holder.mytask_report.setVisibility(ViewGroup.GONE);
				holder.mymtask_item_content.setVisibility(ViewGroup.VISIBLE);
			}
		} else {
			holder.mytask_report.setVisibility(ViewGroup.GONE);
		}
		holder.mytask_report.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mListener != null) {
					mListener.onRightItemClick(arg0, position);
				}
			}
		});
		if (Stafforg.equals("edit")) {
			holder.mymtask_item_content.setTextColor(mContext.getResources().getColor(R.color.dark_blue));// 汇报内容
			holder.mymtask_item_content
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if (mListener != null) {
								mListener.onRightItemClick(arg0, position);
							}
						}
					});
		}
		holder.mymtask_item_title.setText(name);// 标题
		if(null!=Notes){
			holder.mymtask_item_content.setText(Html.fromHtml(Notes));// 汇报内容			
		}else {
			holder.mymtask_item_content.setText("");// 汇报内容		
		}

		holder.mymtask_item_time.setText(Time);// 汇报时间

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
