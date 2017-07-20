package com.meiyin.erp.entity;

public class IT_StayEvent {
	
	
	private String id;//id
	private String event_level;//优先级
	private String title;//事件标题
	private String source;//事件来源
	private String event_type;//事件类型
	private String state;//事件状态
	private String build_time;//新建事件
	private String event_no;//事件编号
	private String modetype;//事件
	private String reciver;//reciver
	private String reciverName;//name
	
	public IT_StayEvent(){
		
	}

	public IT_StayEvent(String id, String event_level, String title,
			String source, String event_type, String state, String build_time,
			String event_no, String modetype, String reciver, String reciverName) {
		this.id = id;
		this.event_level = event_level;
		this.title = title;
		this.source = source;
		this.event_type = event_type;
		this.state = state;
		this.build_time = build_time;
		this.event_no = event_no;
		this.modetype = modetype;
		this.reciver = reciver;
		this.reciverName = reciverName;
	}


	public String getReciverName() {
		return reciverName;
	}

	public void setReciverName(String reciverName) {
		this.reciverName = reciverName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEvent_level() {
		return event_level;
	}

	public void setEvent_level(String event_level) {
		this.event_level = event_level;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBuild_time() {
		return build_time;
	}

	public void setBuild_time(String build_time) {
		this.build_time = build_time;
	}

	public String getEvent_no() {
		return event_no;
	}

	public void setEvent_no(String event_no) {
		this.event_no = event_no;
	}

	public String getModetype() {
		return modetype;
	}

	public void setModetype(String modetype) {
		this.modetype = modetype;
	}

	public String getReciver() {
		return reciver;
	}

	public void setReciver(String reciver) {
		this.reciver = reciver;
	}


}
