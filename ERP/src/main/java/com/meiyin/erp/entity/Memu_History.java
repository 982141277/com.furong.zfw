package com.meiyin.erp.entity;

/**
 * 申请单审批历史
 */
public class Memu_History {
	String appUser; //申请人
	String dept;//所属部门
	String app_view;//审批人
	String app_state;//提交状态
	String app_date;//审核时间
	public Memu_History(String appUser, String dept, String app_view,
			String app_state, String app_date) {
		this.appUser = appUser;
		this.dept = dept;
		this.app_view = app_view;
		this.app_state = app_state;
		this.app_date = app_date;
	}
	public String getAppUser() {
		return appUser;
	}
	public void setAppUser(String appUser) {
		this.appUser = appUser;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getApp_view() {
		return app_view;
	}
	public void setApp_view(String app_view) {
		this.app_view = app_view;
	}
	public String getApp_state() {
		return app_state;
	}
	public void setApp_state(String app_state) {
		this.app_state = app_state;
	}
	public String getApp_date() {
		return app_date;
	}
	public void setApp_date(String app_date) {
		this.app_date = app_date;
	}
	
	
}
