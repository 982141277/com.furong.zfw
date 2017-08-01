package com.meiyin.erp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiyin.erp.R;
import com.meiyin.erp.entity.Meiyinnews;
import com.meiyin.erp.util.DateUtil;

import java.lang.reflect.Field;

public class Xxls_Adapter extends Adapter<Meiyinnews> {

	private Context mContext;
	private LayoutInflater inflater;
	Intent intent;

	public Xxls_Adapter(Context mContext) {
		super(mContext);
		// TODO Auto-generated constructor stub
		intent = new Intent();
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);
	}

	static class ViewHolder {
		TextView leixin;
		TextView time;
		ImageView img;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.xxls_item, null);
			holder = new ViewHolder();
			holder.leixin = (TextView) convertView.findViewById(R.id.leixin);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.img = (ImageView) convertView.findViewById(R.id.imgs);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Meiyinnews list = mList.get(position);
		String name = list.getName();
		Long time = list.getTime();
		
		String dsString = DateUtil.convertLongToDate(time).substring(8,
				10);
		String dates = DateUtil.getHourTimeStr(time).substring(0, 5);
		String S = DateUtil.convertLong00(System.currentTimeMillis());
		Long h = DateUtil.getLongFromStr(S);
		long t = h - time;
		long times = t / 1000 / 3600;
		if (times > 24 && times < 48) {
			holder.time.setText("前日 " + dates);
		} else if (times < 24 && times > 0) {
			holder.time.setText("昨日 " + dates);
		} else if (times < 0) {
			holder.time.setText("今日 " + dates);
		} else if (times > 48) {
			holder.time.setText(dsString + "日 " + dates);
		}
		// final String code=list.getSystem_code();
		// Long sz = list.getSize();
		// if(sz<1){
		// holder.relativelaouts.setVisibility(ViewGroup.GONE);
		// }else{
		// holder.relativelaouts.setVisibility(ViewGroup.VISIBLE);
		// }
		holder.leixin.setText(Html.fromHtml(name,imgGetter, null));

		if (list.getBiaoshi().equals("0")) {
			holder.img.setVisibility(ViewGroup.VISIBLE);
		} else {
			holder.img.setVisibility(ViewGroup.GONE);
		}
		// holder.hongdiandian.setText(""+sz);

		return convertView;

	}
	ImageGetter imgGetter = new ImageGetter() {
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
