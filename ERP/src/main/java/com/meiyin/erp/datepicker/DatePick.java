package com.meiyin.erp.datepicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.meiyin.erp.R;

import java.util.Calendar;

public class DatePick {

	private Context context;
	private LayoutInflater inflater = null;
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private WheelView min;
	private WheelView sec;
	private TextView Datetime;

	public DatePick(Context context, TextView Datetime) {
		this.context = context;
		this.Datetime = Datetime;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getDataPick() {
		Calendar c = Calendar.getInstance();
		int norYear = c.get(Calendar.YEAR)+1;
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH);
		;
		int curDate = c.get(Calendar.DAY_OF_MONTH);
		int curmin = c.get(Calendar.HOUR);
		int cursec = c.get(Calendar.MINUTE);

		View view = inflater.inflate(R.layout.wheel_date_picker, null);

		year = (WheelView) view.findViewById(R.id.year);// 年
		NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(
				context, 1950, norYear);
		numericWheelAdapter1.setLabel("年");
		year.setViewAdapter(numericWheelAdapter1);
		year.setCyclic(true);// 是否可循环滑动
		year.addScrollingListener(scrollListener);

		month = (WheelView) view.findViewById(R.id.month);// 月
		NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(
				context, 01, 12, "%02d");
		numericWheelAdapter2.setLabel("月");
		month.setViewAdapter(numericWheelAdapter2);
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day = (WheelView) view.findViewById(R.id.day);// 天
		day.setCyclic(true);
		day.addScrollingListener(scrollListener);
		initDay(curYear, curMonth);

		min = (WheelView) view.findViewById(R.id.min);// 小时
		NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(
				context, 00, 23, "%02d");
		numericWheelAdapter3.setLabel("时");
		min.setViewAdapter(numericWheelAdapter3);
		min.setCyclic(true);
		min.addScrollingListener(scrollListener);

		sec = (WheelView) view.findViewById(R.id.sec);// 分钟
		NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(
				context, 00, 59, "%02d");
		numericWheelAdapter4.setLabel("分");
		sec.setViewAdapter(numericWheelAdapter4);
		sec.setCyclic(true);
		sec.addScrollingListener(scrollListener);

		year.setVisibleItems(7);// 设置显示行数
		month.setVisibleItems(7);
		day.setVisibleItems(7);
		min.setVisibleItems(7);
		sec.setVisibleItems(7);

		year.setCurrentItem(curYear - 1950);
		month.setCurrentItem(curMonth);
		day.setCurrentItem(curDate - 1);
		min.setCurrentItem(curmin - 1);
		sec.setCurrentItem(cursec - 1);

		return view;
	}

	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = year.getCurrentItem() + 1950;// 年
			int n_month = month.getCurrentItem() + 1;// 月

			initDay(n_year, n_month);
			int nday = day.getCurrentItem() + 1;
			int nmin = min.getCurrentItem();
			int nsec = sec.getCurrentItem();

			StringBuilder sb = new StringBuilder();
			sb.append(n_year);
			sb.append("-");
			sb.append(n_month < 10 ? "0" + (n_month) : (n_month));
			sb.append("-");
			sb.append(((nday) < 10) ? "0" + (nday) : (nday));
			sb.append(" ");
			sb.append(((nmin) < 10) ? "0" + (nmin) : (nmin));
			sb.append(":");
			sb.append(((nsec) < 10) ? "0" + (nsec) : (nsec));
			sb.append(":00");

			Datetime.setText(sb.toString());
		}
	};

	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	/**
	 */
	private void initDay(int arg1, int arg2) {
		NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(
				context, 1, getDay(arg1, arg2), "%02d");
		numericWheelAdapter.setLabel("日");
		day.setViewAdapter(numericWheelAdapter);
	}
}
