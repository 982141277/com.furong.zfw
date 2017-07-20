package com.meiyin.erp.entity;

public class IT_EventSp_Entity {

	private String value;
	private String key;
	
	public IT_EventSp_Entity(String value, String key) {
		this.value = value;
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
