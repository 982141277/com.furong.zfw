package com.meiyin.erp.entity;

public class IT_Management_Event_Entity {

	String id;//事件id
	String event_no; //事件编号
	String title; //事件标题
	String source;//事件来源
	String event_type;//事件类型
	String event_level; //优先级
	String build_time; //新建时间
	String state; //事件状态
	
/*	Boolean boss;//领导
	String bg_address; //办公地址
	String lx_phone;//联系电话
	String chuliren; //处理人
	String chulishichang; //处理时常
*/
	public IT_Management_Event_Entity(){
		
	}

	public IT_Management_Event_Entity(String id, String event_no, String title,
		String source, String event_type, String event_level,
		String build_time, String state) {
	this.id = id;
	this.event_no = event_no;
	this.title = title;
	this.source = source;
	this.event_type = event_type;
	this.event_level = event_level;
	this.build_time = build_time;
	this.state = state;
}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEvent_no() {
		return event_no;
	}

	public void setEvent_no(String event_no) {
		this.event_no = event_no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getEvent_type() {
		return event_type;
	}

	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}

	public String getEvent_level() {
		return event_level;
	}

	public void setEvent_level(String event_level) {
		this.event_level = event_level;
	}

	public String getBuild_time() {
		return build_time;
	}

	public void setBuild_time(String build_time) {
		this.build_time = build_time;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	
}
