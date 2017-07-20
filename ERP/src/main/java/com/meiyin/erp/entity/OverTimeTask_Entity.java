package com.meiyin.erp.entity;


import com.meiyin.erp.bean.TreeNodeId;
import com.meiyin.erp.bean.TreeNodeLabel;
import com.meiyin.erp.bean.TreeNodePid;

/*
 * 所有人员信息
 */
public class OverTimeTask_Entity {

	@TreeNodeId
	private int id;
	@TreeNodePid
	private int pid=0;
	@TreeNodeLabel
	private String name;
	private String user_code;
	private String osequence;
	private String telephone;
	private String phone_num;

	public OverTimeTask_Entity(int id, int pid, String name, String user_code,
			String osequence, String telephone, String phone_num) {
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.user_code = user_code;
		this.osequence = osequence;
		this.telephone = telephone;
		this.phone_num = phone_num;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUser_code() {
		return user_code;
	}
	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getOsequence() {
		return osequence;
	}
	public void setOsequence(String osequence) {
		this.osequence = osequence;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPhone_num() {
		return phone_num;
	}

	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}
	
}
