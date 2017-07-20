package com.meiyin.erp.adapter;



import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiyin.erp.R;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.Personalityentity;

/**
 * @date 2016/7/25
 * @description Gridview_adapter
 */
public class PersonalityAdapter extends Adapter<Personalityentity> {

	
	private Context mContext;
	private LayoutInflater inflater;
	private int a=0;
	private SharedPreferences sp;
	public PersonalityAdapter(Context mContext) {
		super(mContext);
		 sp = mContext.getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		 a=sp.getInt(SPConstant.BACKGROUND, 0);
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);
	}

	public void bl(int a){
		this.a=a;
	}
	public static class ViewHolder {
		private ImageView IMG;
		private TextView text;
		private ImageView xuanzhe;


	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.gridview_item, null);
			holder = new ViewHolder();
			holder.IMG = (ImageView) convertView.findViewById(R.id.back_img);
			holder.text = (TextView) convertView
					.findViewById(R.id.text);
			holder.xuanzhe = (ImageView) convertView.findViewById(R.id.xuanzhe);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Personalityentity memulists = mList.get(position);
		String Name = memulists.getText();
		Drawable Img = memulists.getImg();

		holder.text.setText(Name);
		holder.IMG.setImageDrawable(Img);	

		if(position==a){
			holder.xuanzhe.setVisibility(ViewGroup.VISIBLE);
		}else{
			holder.xuanzhe.setVisibility(ViewGroup.GONE);
		}
		return convertView;
	}
}
