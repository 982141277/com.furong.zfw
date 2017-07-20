package com.meiyin.erp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.meiyin.erp.R;
import com.meiyin.erp.adapter.Adapter;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.util.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * 任务日志汇报详情
 */
public class MyTaskReportDetailsActivity extends Activity{

	private SharedPreferences sp ;
	private Context context ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mytaskreportdetail_mian);
		context = getApplicationContext();
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		/*
		 * 初始化
		 */
		initHeader();
		initView();

	}
	/*
	 * 初始化标题UI
	 */
	private void initHeader() {
		ImageView headtitleimageView = (ImageView) findViewById(R.id.headtitleimageView);
		headtitleimageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MyTaskReportDetailsActivity.this.finish();
			}
		});
		TextView headtitletext = (TextView) findViewById(R.id.headtitletext);
		headtitletext.setText("汇报详情");
	}
	private void initView(){
		ArrayList<MyTaskReportDetail>list=new ArrayList<MyTaskReportDetail>();
		String taskreport=getIntent().getStringExtra("taskreport");
		try {
			JSONArray array=new JSONArray(taskreport);
			for (int i = 0; i < array.length(); i++) {
				String staff_name=array.getJSONObject(i).getString("staff_name");
				String report_notes=array.getJSONObject(i).getString("report_notes");
				String report_time=array.getJSONObject(i).getString("report_time");
				list.add(new MyTaskReportDetail(staff_name,report_notes,report_time));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MyTaskReportDetailAdapter adapter = new MyTaskReportDetailAdapter(context);
		ListView mytask_report_detail_list = (ListView)findViewById(R.id.mytask_report_detail_list);
		adapter.setList(list);
		mytask_report_detail_list.setAdapter(adapter);
	}

	class MyTaskReportDetail {
	private String name ;
	private String content ;
	private String time ;

	public MyTaskReportDetail(String name, String content, String time) {
		this.name = name;
		this.content = content;
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	}

	class MyTaskReportDetailAdapter extends Adapter<MyTaskReportDetail> {
		private Context mContext;
		private LayoutInflater inflater;
		
		public MyTaskReportDetailAdapter(Context mContext) {
			super(mContext);
			this.mContext=mContext;
			this.inflater = LayoutInflater.from(mContext);
			// TODO Auto-generated constructor stub
		}
		public class ViewHolder {
			TextView mytask_report_name;//名称
			TextView mytask_report_time;//时间
			TextView mytask_report_detail_content1;//内容
			TextView mytask_report_detail_content2;//内容
			TextView mytask_report_fulltext;//全文
			TextView mytask_report_stoptext;//收起
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.mytask_report_details_item, null);
				holder = new ViewHolder();
				holder.mytask_report_name = (TextView) convertView
						.findViewById(R.id.mytask_report_name);
				holder.mytask_report_time = (TextView) convertView
						.findViewById(R.id.mytask_report_time);
				
				holder.mytask_report_detail_content1 = (TextView) convertView
						.findViewById(R.id.mytask_report_detail_content1);
				holder.mytask_report_detail_content2 = (TextView) convertView
						.findViewById(R.id.mytask_report_detail_content2);
				holder.mytask_report_fulltext = (TextView) convertView
						.findViewById(R.id.mytask_report_fulltext);
				holder.mytask_report_stoptext = (TextView) convertView
						.findViewById(R.id.mytask_report_stoptext);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			MyTaskReportDetail lists = mList.get(position);
			String content = lists.getContent();
			String name = lists.getName();
			String Time = lists.getTime();
			holder.mytask_report_name.setText(name);
			holder.mytask_report_time.setText(DateUtil.getEnglishTime(Time));
			holder.mytask_report_detail_content1.setText(content);
			holder.mytask_report_detail_content2.setText(content);

			if(content.length()<52){
				holder.mytask_report_fulltext.setVisibility(ViewGroup.GONE);
			}
			holder.mytask_report_fulltext.setOnClickListener(new onclick(true,holder.mytask_report_fulltext,holder.mytask_report_stoptext,holder.mytask_report_detail_content1,holder.mytask_report_detail_content2));
			holder.mytask_report_stoptext.setOnClickListener(new onclick(false,holder.mytask_report_fulltext,holder.mytask_report_stoptext,holder.mytask_report_detail_content1,holder.mytask_report_detail_content2));
			return convertView;
		}
		class onclick implements OnClickListener{
			private TextView fulltext;
			private TextView stoptext;
			private TextView detail_content1;
			private TextView detail_content2;
			private boolean bool;
			
			private onclick(boolean bool,TextView fulltext,TextView stoptext,TextView detail_content1,TextView detail_content2){
				this.bool=bool;
				this.fulltext=fulltext;
				this.stoptext=stoptext;
				this.detail_content1=detail_content1;
				this.detail_content2=detail_content2;
			}
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub\
				if(bool){
				fulltext.setVisibility(ViewGroup.GONE);
				stoptext.setVisibility(ViewGroup.VISIBLE);
				detail_content1.setVisibility(ViewGroup.GONE);
				detail_content2.setVisibility(ViewGroup.VISIBLE);
				}else{
					fulltext.setVisibility(ViewGroup.VISIBLE);
					stoptext.setVisibility(ViewGroup.GONE);
					detail_content1.setVisibility(ViewGroup.VISIBLE);
					detail_content2.setVisibility(ViewGroup.GONE);
				}
			}
		}
		}
}

	
	
