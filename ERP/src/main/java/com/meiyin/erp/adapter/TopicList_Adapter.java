package com.meiyin.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiyin.erp.entity.Topic_Entity;
import com.meiyin.erp.R;
import com.meiyin.erp.util.DateUtil;

public class TopicList_Adapter extends Adapter<Topic_Entity>{
	private Context mContext;
	private LayoutInflater inflater;
	
	public TopicList_Adapter(Context mContext) {
		super(mContext);
		this.mContext=mContext;
		this.inflater = LayoutInflater.from(mContext);
		// TODO Auto-generated constructor stub
	}
	public static class ViewHolder {
		public TextView topic_title;//标题
		public TextView topic_unreadcount;//未读人数
		public TextView topic_clickcount;//点击数
		public TextView topic_readcount;//已读人数
		public TextView topic_create_time;//创建时间
		public TextView readflag;//是否查看
		public ImageView topic_imgs;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.topic_item, null);
			holder = new ViewHolder();
			holder.topic_title = (TextView) convertView
					.findViewById(R.id.topic_title);
			holder.topic_unreadcount = (TextView) convertView
					.findViewById(R.id.topic_unreadcount);
			holder.topic_clickcount = (TextView) convertView
					.findViewById(R.id.topic_clickcount);
			holder.topic_readcount = (TextView) convertView
					.findViewById(R.id.topic_readcount);
			holder.topic_create_time = (TextView) convertView
					.findViewById(R.id.topic_create_time);
			
			holder.topic_imgs = (ImageView) convertView
					.findViewById(R.id.topic_imgs);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Topic_Entity lists = mList.get(position);
		String Title = lists.getTitle();//标题
		String Unreadcount = lists.getUnreadcount();//未读人数
//		String Clickcount = lists.getClickcount();//点击数
		String Readcount = lists.getReadcount();//已经阅读数
		Long Create_time = lists.getCreate_time();
		boolean Readflag = lists.getReadflag();//是否查看
		if(Readflag){
			holder.topic_imgs.setVisibility(ViewGroup.GONE);
		}else{
			holder.topic_imgs.setVisibility(ViewGroup.VISIBLE);
		}
		
		holder.topic_title.setText(position+1+"、"+Title);
		holder.topic_unreadcount.setText(Unreadcount);
		
//		holder.topic_clickcount.setText(Html.fromHtml("阅读 </font><font color=\"#ED1C24\">"+Readcount+"</font> \t\t 点击 </font><font color=\"#ED1C24\">"+Clickcount+"</font>"));
		holder.topic_readcount.setText(Readcount);
		String dsString = DateUtil.convertLongToDate(Create_time);
		holder.topic_create_time.setText(dsString);
		String ent = DateUtil.convertLongToDate(Create_time).substring(5,
				10);
		Long ts = DateUtil.getLongFromStr(dsString);
		String dates = DateUtil.getHourTimeStr(ts).substring(0, 5);
		String S = DateUtil.convertLong00(System.currentTimeMillis());
		Long h = DateUtil.getLongFromStr(S);
		long t = h - ts;
		long times = t / 1000 / 3600;
		if (times > 24 && times < 48) {
			holder.topic_create_time.setText("前日 " + dates);
		} else if (times < 24 && times > 0) {
			holder.topic_create_time.setText("昨日 " + dates);
		} else if (times < 0) {
			holder.topic_create_time.setText("今日 " + dates);
		} else if (times > 48) {
			holder.topic_create_time.setText(ent + " " + dates);
		}
		return convertView;
	}
}
