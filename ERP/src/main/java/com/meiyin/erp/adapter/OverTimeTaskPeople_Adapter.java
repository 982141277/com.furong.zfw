package com.meiyin.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meiyin.erp.entity.OverTimeTaskPeople_Entity;
import com.meiyin.erp.R;

public class OverTimeTaskPeople_Adapter extends Adapter<OverTimeTaskPeople_Entity>{
	private Context mContext;
	private LayoutInflater inflater;
	private String type;
	public OverTimeTaskPeople_Adapter(Context mContext,String type) {
		super(mContext);
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);
		this.type=type;
		// TODO Auto-generated constructor stub
	}
	public static class ViewHolder {
		TextView overtimetask_people;
		TextView overtimetask_type;
		TextView overtimetask_prover;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.overtimetaskpeople_item, null);
			holder = new ViewHolder();
			holder.overtimetask_people = (TextView) convertView
					.findViewById(R.id.overtimetask_people);
			holder.overtimetask_type = (TextView) convertView
					.findViewById(R.id.overtimetask_type);
			holder.overtimetask_prover = (TextView) convertView
					.findViewById(R.id.overtimetask_prover);


			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.overtimetask_prover.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mListener != null) {
					mListener.onRightItemClick(arg0, position);
				}
			}
		});
		OverTimeTaskPeople_Entity memulists = mList.get(position);
		String Username = memulists.getUsername();
		String state=memulists.getState();

		holder.overtimetask_people.setText(Username);
		if(state.equals("3")){
			holder.overtimetask_type.setText("已审批详情");
			if(type.equals("1")){
				holder.overtimetask_prover.setVisibility(ViewGroup.GONE);				
			}else if(type.equals("2")){
				holder.overtimetask_prover.setVisibility(ViewGroup.VISIBLE);
			}

		}else{
			holder.overtimetask_type.setText("已填写详情");	
			holder.overtimetask_type.setTextColor(mContext.getResources().getColor(R.color.text_green));
			holder.overtimetask_prover.setVisibility(ViewGroup.VISIBLE);
		}
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
