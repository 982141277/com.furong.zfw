package com.meiyin.erp.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meiyin.erp.entity.MyTaskListEntity;
import com.meiyin.erp.R;
import com.meiyin.erp.util.DateUtil;

public class MyTaskListAdapter extends Adapter<MyTaskListEntity> {
		private Context mContext;
		private LayoutInflater inflater;

		public MyTaskListAdapter(Context mContext) {
			super(mContext);
			this.mContext = mContext;
			this.inflater = LayoutInflater.from(mContext);
			// TODO Auto-generated constructor stub
		}

		public static class ViewHolder {
			public TextView mytasklist_item_title;// 标题
			public TextView mytasklist_item_type;//状态
			public TextView mymtask_item_time;//时间
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.mytasklist_item, null);
				holder = new ViewHolder();
				holder.mytasklist_item_title = (TextView) convertView
						.findViewById(R.id.mytasklist_item_title);
				holder.mytasklist_item_type = (TextView) convertView
						.findViewById(R.id.mytasklist_item_type);
				holder.mymtask_item_time = (TextView) convertView
						.findViewById(R.id.mymtask_item_time);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			MyTaskListEntity lists = mList.get(position);
			
			String mytasklisttitle = lists.getTask_name();
			String mytasklisttype = "";
			String Creat_username= lists.getCreat_username();
			String Operator= lists.getOperatorname();
			String Knower= lists.getKnowername();
			
			if(!Knower.equals("")){
				mytasklisttype="<font color=\"#ED1C24\">"+Creat_username+"</font>下达任务\t\t<font color=\"#ED1C24\">"+Operator+"</font>执行\t\t<font color=\"#ED1C24\">"+Knower+"</font>知晓";
			}else{
				mytasklisttype="<font color=\"#ED1C24\">"+Creat_username+"</font>下达任务\t\t<font color=\"#ED1C24\">"+Operator+"</font>执行";

			}

			String Start_time= DateUtil.getEnglishTime(lists.getStart_time());
			String End_time = DateUtil.getEnglishTime(lists.getEnd_time());

			String mytasklisttime=Start_time.substring(0,Start_time.length()-8)+" 至  "+End_time.substring(0,Start_time.length()-8);
			holder.mytasklist_item_title.setText(mytasklisttitle);// 标题
			holder.mytasklist_item_type.setText(Html.fromHtml(mytasklisttype));// 类型
			holder.mymtask_item_time.setText(mytasklisttime);// 工作时间
			
			return convertView;
		}

	
}
