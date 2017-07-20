package com.meiyin.erp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的Adapter
 */
public class Adapter<T> extends BaseAdapter {

	protected Context mContext;

	protected ArrayList<T> mList;

	public Adapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int setList(List<T> list) {
		mList = null;
		return addList(list);
	}

	public int addList(List<T> list) {
		if (mList == null) {
			mList = new ArrayList<T>();
		}
		if (list != null) {
			mList.addAll(list);
		}
		return mList == null ? 0 : mList.size();
	}

	public ArrayList<T> getList() {
		return mList;
	}

	public void setArray(T[] list) {
		ArrayList<T> arrayList = new ArrayList<T>(list.length);
		for (T t : list) {
			arrayList.add(t);
		}
		setList(arrayList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return convertView;
	}
}
