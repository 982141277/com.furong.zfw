package com.meiyin.erp.entity;

/*
 * 事件详细处理情况
 */
public class IT_Event_SQ_Entity {

	private String content;//处理信息
	private String username; //处理人
	private String oper_time;//处理时间
	private String state;//环节名称
	
	
	public IT_Event_SQ_Entity(String content, String username,
			String oper_time, String state) {
		super();
		this.content = content;
		this.username = username;
		this.oper_time = oper_time;
		this.state = state;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getOper_time() {
		return oper_time;
	}


	public void setOper_time(String oper_time) {
		this.oper_time = oper_time;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}
	
	


	
}
