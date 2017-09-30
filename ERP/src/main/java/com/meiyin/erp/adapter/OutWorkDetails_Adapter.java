package com.meiyin.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meiyin.erp.entity.OutWorkDetails_Entity;
import com.meiyin.erp.R;

public class OutWorkDetails_Adapter extends Adapter<OutWorkDetails_Entity>{
	private Context mContext;
	private LayoutInflater inflater;
	
	public OutWorkDetails_Adapter(Context mContext) {
		super(mContext);
		this.mContext=mContext;
		this.inflater = LayoutInflater.from(mContext);
		// TODO Auto-generated constructor stub
	}
	public static class ViewHolder {
		private TextView clientaddress_text;//客户地址
		private TextView clientname_text;//客户名称
		private TextView brushaddress_text;//刷卡地址
		private TextView brushtime_text;//刷卡时间
		private TextView isbrush_text;//是否刷卡
		private TextView brush_text;//刷卡按钮
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if ( null==convertView) {
			convertView = inflater.inflate(
					R.layout.outworkdetails_item, null);
			holder = new ViewHolder();
			holder.clientaddress_text = (TextView) convertView
					.findViewById(R.id.clientaddress_text);
			holder.clientname_text = (TextView) convertView
					.findViewById(R.id.clientname_text);
			holder.brushaddress_text = (TextView) convertView
					.findViewById(R.id.brushaddress_text);
			holder.brushtime_text = (TextView) convertView
					.findViewById(R.id.brushtime_text);
			holder.isbrush_text = (TextView) convertView
					.findViewById(R.id.isbrush_text);
			holder.brush_text = (TextView) convertView
					.findViewById(R.id.brush_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		OutWorkDetails_Entity lists = mList.get(position);
		
		String ClientName = lists.getTargetname();
		String ClientAddress=lists.getTargetaddress();
		String BrushAddress=lists.getBrushaddress();
		String BrushTime=lists.getBrushtime();
		String IsBrush=lists.getIsbrush();
		String Brush_type=lists.getBrush_type();

		if(Brush_type.equals("1")){
			holder.clientname_text.setText("外出办事"+" (到达)");
		}else if(Brush_type.equals("11")){
			holder.clientname_text.setText("外出办事"+" (离开)");
		}else if(Brush_type.equals("2")){
		holder.clientname_text.setText(ClientName+" (到达)");//客户名称
		}else if(Brush_type.equals("21")){
		holder.clientname_text.setText(ClientName+" (离开)");//客户名称
		}
		holder.clientaddress_text.setText(ClientAddress);//客户地址
		holder.brushaddress_text.setText(BrushAddress);//刷卡地址
		holder.brushtime_text.setText(BrushTime);//刷卡时间
		holder.isbrush_text.setText(IsBrush);//是否刷卡
		if(IsBrush.equals("1")){
			holder.brush_text.setVisibility(ViewGroup.GONE);
			holder.brushaddress_text.setVisibility(ViewGroup.VISIBLE);
			holder.brushtime_text.setVisibility(ViewGroup.VISIBLE);
			holder.isbrush_text.setText("已打卡");
			holder.isbrush_text.setTextColor(mContext.getResources().getColor(R.color.text_green));
		}else if(IsBrush.equals("0")){
			holder.brush_text.setVisibility(ViewGroup.VISIBLE);
			holder.brushaddress_text.setVisibility(ViewGroup.GONE);
			holder.brushtime_text.setVisibility(ViewGroup.GONE);
			holder.isbrush_text.setText("未打卡");
			holder.isbrush_text.setTextColor(mContext.getResources().getColor(R.color.dark_red));
		}else if(IsBrush.equals("2")){
			holder.brush_text.setVisibility(ViewGroup.GONE);
			holder.brushaddress_text.setVisibility(ViewGroup.VISIBLE);
			holder.brushtime_text.setVisibility(ViewGroup.VISIBLE);
			holder.isbrush_text.setText("打卡异常");
			holder.isbrush_text.setTextColor(mContext.getResources().getColor(R.color.dark_red));
	}
		holder.brush_text.setOnClickListener(new View.OnClickListener() {
			
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
	private onAdapterItemClick mListener = null;
	
	public void setOnRightItemClickListener(onAdapterItemClick listener) {
		mListener = listener;
	}

}