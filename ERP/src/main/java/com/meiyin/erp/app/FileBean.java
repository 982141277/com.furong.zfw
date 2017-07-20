package com.meiyin.erp.app;


import com.meiyin.erp.bean.TreeNodeId;
import com.meiyin.erp.bean.TreeNodeLabel;
import com.meiyin.erp.bean.TreeNodePid;

public class FileBean
{
	@TreeNodeId
	private int _id;
	@TreeNodePid
	private int parentId;
	@TreeNodeLabel
	private String name;
	private long length;
	private String desc;
	private boolean isChecked;//是否选中
	public FileBean(int _id, int parentId, String name,boolean isChecked)
	{
		super();
		this._id = _id;
		this.parentId = parentId;
		this.name = name;
		this.isChecked=isChecked;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}


}
