package com.meiyin.erp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meiyin.erp.R;
import com.meiyin.erp.activity.Memulist;
import com.meiyin.erp.entity.Memulist_Entity;
import com.meiyin.erp.util.DateUtil;

public class Memulist_Adapter extends Adapter<Memulist_Entity> {

	private Context mContext;
	private Memulist memulist;
	private LayoutInflater inflater;

	public Memulist_Adapter(Context mContext, Memulist memulist) {
		super(mContext);
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.memulist = memulist;
		this.inflater = LayoutInflater.from(mContext);
	}

	public static class ViewHolder {
		private TextView Type;
		private TextView Applicant;
		private TextView Flowstate;
		private TextView Flowcaption;
		private TextView Applycause;
		private TextView Section;
		private TextView CreateTime;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.memulist_model, null);
			holder = new ViewHolder();
			holder.Type = (TextView) convertView.findViewById(R.id.text_type);
			holder.Applicant = (TextView) convertView
					.findViewById(R.id.text_shenqingren);
			holder.Flowstate = (TextView) convertView
					.findViewById(R.id.liuchengzhuangtai);
			holder.Flowcaption = (TextView) convertView
					.findViewById(R.id.liuchengshuoming);
			holder.Applycause = (TextView) convertView
					.findViewById(R.id.shenqingyuanyin);
			holder.Section = (TextView) convertView
					.findViewById(R.id.suoshubumen);
			holder.CreateTime = (TextView) convertView
					.findViewById(R.id.text_time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Memulist_Entity memulists = mList.get(position);
		String Type = memulists.getType();
		String Applicant = memulists.getApplicant();
		String Flowstate = memulists.getFlowstate();
		String Flowcaption = memulists.getFlowcaption();
		String Applycause = memulists.getApplycause();
		String Section = memulists.getSection();
		Long CreateTime = memulists.getCreateTime();
		String maincontent = memulists.getMainContent();
		String Leave_reason = memulists.getLeave_reason();
		String ApplyDesc = memulists.getApplyDesc();
		String Reason = memulists.getReason();
		
		StringBuffer sb = new StringBuffer();
		holder.Type.setText(Type.substring(0, 2));
		if(null!=Applicant){
			holder.Applicant.setText(Applicant);			
		}else {
			holder.Applicant.setText("");
		}
		try {
			
		if(null!=Flowstate){
			holder.Flowstate.setText(Flowstate);			
		}else {
			holder.Flowstate.setText("");
		}
		if (null != Flowcaption) {
			if(Flowcaption.equals("无")){
				holder.Flowcaption.setText(Html.fromHtml(Flowstate));
				holder.Flowcaption.setTextColor(mContext.getResources()
						.getColor(R.color._red));
			}else if (Flowcaption.contains("审批")) {
				String f = "</font><font color=\"#9a9a9a\">审批</font>";
				String ls = Flowcaption.replace("审批", f);
				String s = "<font color=\"#ED1C24\">"+ ls;
				holder.Flowcaption.setText(Html.fromHtml(s));
			} else if (Flowcaption.contains("(")) {
				if (Flowcaption.contains("<")) {
					int start = Flowcaption.indexOf("<");
					int end = Flowcaption.lastIndexOf(">");
					String gg = Flowcaption.substring(start, end + 1);
					int start1 = gg.indexOf("(");
					int end1 = gg.indexOf(")");
					String gg1 = gg.substring(start1, end1 + 1);
					String tou = Flowcaption.substring(0, start);
					String wei = "";
					if (Flowcaption.length() > end) {
						wei = Flowcaption.substring(end + 1);
					}
					sb.append("<font color=\"#ED1C24\">" + tou + "</font>");
					sb.append("<font color=\"#ED1C24\">" + gg1 + "</font>");
					sb.append("<font color=\"#9a9a9a\">" + wei + "</font>");
				} else {
					int start2 = Flowcaption.indexOf("(");
					int end2 = Flowcaption.indexOf(")");
					String gg2 = Flowcaption.substring(start2, end2 + 1);
					String tou2 = Flowcaption.substring(0, start2);
					String wei2 = "";
					if (Flowcaption.length() > end2) {
						wei2 = Flowcaption.substring(end2 + 1);
					}
					sb.append("<font color=\"#ED1C24\">" + tou2 + "</font>");
					sb.append("<font color=\"#ED1C24\">" + gg2 + "</font>");
					sb.append("<font color=\"#9a9a9a\">" + wei2 + "</font>");
				}

				holder.Flowcaption.setText(Html.fromHtml(sb.toString()));
				holder.Flowcaption.setTextColor(mContext.getResources()
						.getColor(R.color._red));
			} else if (!Flowcaption.contains("审批")){
				holder.Flowcaption.setText(Html.fromHtml(Flowcaption));
				holder.Flowcaption.setTextColor(mContext.getResources()
						.getColor(R.color._red));
			}

		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (Applycause.equals("")) {
			
			if(Type.equals("加班任务单")||Type.equals("外出申请单")){
				if(null!=maincontent){
					holder.Applycause.setText(maincontent);				
				}				
			}else if(Type.equals("请假申请单")){
				if(null!=Leave_reason){
					holder.Applycause.setText(Leave_reason);				
				}		
			}else if(Type.equals("费用申请单")){
				if(null!=ApplyDesc){
					holder.Applycause.setText(ApplyDesc);				
				}		
			}else if(Type.equals("请购申请单")){
				if(null!=Reason){
					holder.Applycause.setText(Reason);				
				}	
			}else if(Type.equals("员工转正申请单")){
				holder.Applycause.setText("");
			}


		} else {
			holder.Applycause.setText(Html.fromHtml(Applycause));
		}
		holder.Section.setText("(" + Section + ")");
		String dsString = DateUtil.convertLongToDate(CreateTime).substring(5,
				10);
		String dates = DateUtil.getHourTimeStr(CreateTime).substring(0, 5);
		String S = DateUtil.convertLong00(System.currentTimeMillis());
		Long h = DateUtil.getLongFromStr(S);
		String name=DateUtil.gettimes(h,CreateTime);
		holder.CreateTime.setText(name);
//		long time = h - CreateTime;
//		long times = time / 1000 / 3600;
//
//		if (times > 24 && times < 48) {
//			holder.CreateTime.setText("前日 " + dates);
//		} else if (times < 24 && times > 0) {
//			holder.CreateTime.setText("昨日 " + dates);
//		} else if (times < 0) {
//			holder.CreateTime.setText("今日 " + dates);
//		} else if (times > 48) {
//			holder.CreateTime.setText(dsString + " " + dates);
//		}
		// if (null != Flowstate) {
		// if (Flowstate.equals("结束")) {
		// holder.Flowstate.setTextColor(mContext.getResources().getColor(
		// R.color.text_green));
		// } else if (Flowstate.equals("审批中")) {
		// holder.Flowstate.setTextColor(Color.BLUE);
		// } else if (Flowstate.equals("未审批")) {
		// holder.Flowstate.setTextColor(Color.BLACK);
		// }
		//
		// }
		holder.Type.setTextColor(Color.RED);

		return convertView;
	}

}
