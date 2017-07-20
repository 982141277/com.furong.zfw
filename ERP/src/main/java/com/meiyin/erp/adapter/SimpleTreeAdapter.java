package com.meiyin.erp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.meiyin.erp.R;
import com.meiyin.erp.bean.Node;
import com.meiyin.erp.bean.TreeListViewAdapter;

import java.util.List;

public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T>
{
	public Context context;
	public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas,
			int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException
	{
		super(mTree, context, datas, defaultExpandLevel);
		this.context=context;
	}

	@Override
	public View getConvertView(Node node , int position, View convertView, ViewGroup parent)
	{
		
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.list_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) convertView
					.findViewById(R.id.id_treenode_icon);
			viewHolder.label = (TextView) convertView
					.findViewById(R.id.id_treenode_label);
			viewHolder.id_treeheard = (com.meiyin.erp.ui.CircleImageView) convertView
					.findViewById(R.id.id_treeheard);
			
			convertView.setTag(viewHolder);

		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.id_treeheard.setBorderColor(context.getResources().getColor(R.color._red));
		viewHolder.id_treeheard.setBorderWidth(2);

		if (node.getIcon() == -1)
		{

			viewHolder.icon.setVisibility(View.INVISIBLE);
			viewHolder.id_treeheard.setVisibility(View.VISIBLE);
		} else
		{
			viewHolder.icon.setVisibility(View.VISIBLE);
			viewHolder.id_treeheard.setVisibility(View.GONE);
			viewHolder.icon.setImageResource(node.getIcon());
		}
		viewHolder.label.setText(node.getName());
		
		return convertView;
	}

	private final class ViewHolder
	{
		ImageView icon;
		TextView label;
		com.meiyin.erp.ui.CircleImageView id_treeheard;
	}
}
