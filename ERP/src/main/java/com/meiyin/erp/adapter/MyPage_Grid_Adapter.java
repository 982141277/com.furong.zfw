package com.meiyin.erp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiyin.erp.entity.MyPage_Entity;
import com.meiyin.erp.R;

public class MyPage_Grid_Adapter extends Adapter<MyPage_Entity> {
	private Context mContext;
	private LayoutInflater inflater;

	public MyPage_Grid_Adapter(Context mContext) {
		super(mContext);
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);
		// TODO Auto-generated constructor stub
	}

	public static class ViewHolder {
		public TextView item_mName;// 名称
		public RelativeLayout item_mRelative;//
		public TextView item_xxsize;// 大小
		public ImageView item_mphoto;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.mypage_grid_item, null);
			holder = new ViewHolder();
			holder.item_mphoto = (ImageView) convertView
			.findViewById(R.id.item_mphoto);
			holder.item_mName = (TextView) convertView
					.findViewById(R.id.item_mName);
			holder.item_mRelative = (RelativeLayout) convertView
					.findViewById(R.id.item_mRelative);
			holder.item_xxsize = (TextView) convertView
					.findViewById(R.id.item_xxsize);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MyPage_Entity list = mList.get(position);
		String xName = list.getxName();
		String xMessage = list.getxMessage();
		Drawable draw=list.getImDrawable();
		holder.item_mName.setText(xName);//
		if(null!=draw){
		holder.item_mphoto.setBackgroundDrawable(draw);
		}
		if(xMessage.equals("0")){
			holder.item_mRelative.setVisibility(ViewGroup.GONE);
		}else{
			holder.item_mRelative.setVisibility(ViewGroup.VISIBLE);
			holder.item_xxsize.setText(xMessage);
		}
		return convertView;
	}
}
