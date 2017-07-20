package com.meiyin.erp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meiyin.erp.R;
import com.meiyin.erp.entity.IT_StayEvent;
import com.meiyin.erp.util.DateUtil;

import java.lang.reflect.Field;



public class IT_StayEvent_Adapter  extends Adapter<IT_StayEvent>{

	private Context mContext;
	private LayoutInflater inflater;

	public IT_StayEvent_Adapter(Context mContext) {
		super(mContext);
		this.inflater = LayoutInflater.from(mContext);
		this.mContext = mContext;
	}

	

	private static class ViewHolder {
		private TextView title,state,build_time;
		private TextView Modetype;


	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.stayevent_model, null);
			holder = new ViewHolder();
//			holder.source = (TextView) convertView
//					.findViewById(R.id.sj_source);
			holder.title = (TextView) convertView
					.findViewById(R.id.sj_titles);
			holder.state = (TextView) convertView
					.findViewById(R.id.sj_state);
			holder.build_time = (TextView) convertView
					.findViewById(R.id.sj_build_time);
			holder.Modetype = (TextView) convertView
					.findViewById(R.id.sj_chuli_);
			
			
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		IT_StayEvent list = mList.get(position);
		String Source=list.getSource();
		String Title = list.getTitle();
		String State = list.getState();
		String Time=list.getBuild_time();
		String Modetype=list.getModetype();
		String ReciverName=list.getReciverName();
		holder.title.setText(position+1+"、"+Title+" ("+Source+")");
		if(State.equals("挂起")){
//				holder.state.setText(Html.fromHtml("("+ReciverName+")</font><font color=\"#ED1C24\">"+State+"</font>",imgGetter, null));
				holder.state.setText("("+ReciverName+") "+State);
			}else{
				if(ReciverName==null){
					ReciverName="";
					holder.state.setText(State);
//					holder.state.setText(Html.fromHtml("</font><font color=\"#ED1C24\">"+State+"</font>",imgGetter, null));
				}else{
					holder.state.setText("("+ReciverName+") "+State);
//					holder.state.setText(Html.fromHtml("("+ReciverName+")</font><font color=\"#ED1C24\">"+State+"</font>",imgGetter, null));
				}
		}
		
		String ent = Time.substring(5,
				10);
		Long ts = DateUtil.getLongFromStr(Time);
		String dates = DateUtil.getHourTimeStr(ts).substring(0, 5);
		String S = DateUtil.convertLong00(System.currentTimeMillis());
		Long h = DateUtil.getLongFromStr(S);
		long t = h - ts;
		long times = t / 1000 / 3600;
		if (times > 24 && times < 48) {
			holder.build_time.setText("前日 " + dates);
		} else if (times < 24 && times > 0) {
			holder.build_time.setText("昨日 " + dates);
		} else if (times < 0) {
			holder.build_time.setText("今日 " + dates);
		} else if (times > 48) {
			holder.build_time.setText(ent + " " + dates);
		}
		
		if(Modetype.equals("0")){
			holder.Modetype.setVisibility(ViewGroup.GONE);
		}else if(Modetype.equals("1")){
			holder.Modetype.setVisibility(ViewGroup.VISIBLE);
			holder.Modetype.setText("受理");	
		}else if(Modetype.equals("2")){
			holder.Modetype.setVisibility(ViewGroup.VISIBLE);
			holder.Modetype.setText("主动获取");
		}
		holder.Modetype.setOnClickListener(new View.OnClickListener() {
			
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
	ImageGetter imgGetter = new Html.ImageGetter() {
		@Override
		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			// Log.i("zkr", source);
			int i = 0;
			try {
				Field field = R.drawable.class.getField(source);
				i = field.getInt(new R.drawable());
				drawable = mContext.getResources().getDrawable(i);
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth() - 10,
						drawable.getIntrinsicHeight() - 10);
				// Log.d("zkr", i + "");
			} catch (Exception e) {
				// Log.e("zkr", e.toString());
			}

			return drawable;
		}
	};
}
