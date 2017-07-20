package com.meiyin.erp.entity;

import android.graphics.drawable.Drawable;

public class MyPage_Entity {

	private String xName;
	private String xMessage;
	private Boolean xSelect;
	private Drawable imDrawable;

	

	public MyPage_Entity(String xName, String xMessage, Boolean xSelect,
			Drawable imDrawable) {
		super();
		this.xName = xName;
		this.xMessage = xMessage;
		this.xSelect = xSelect;
		this.imDrawable = imDrawable;
	}

	public Drawable getImDrawable() {
		return imDrawable;
	}

	public void setImDrawable(Drawable imDrawable) {
		this.imDrawable = imDrawable;
	}

	public String getxName() {
		return xName;
	}

	public void setxName(String xName) {
		this.xName = xName;
	}

	public String getxMessage() {
		return xMessage;
	}

	public void setxMessage(String xMessage) {
		this.xMessage = xMessage;
	}

	public Boolean getxSelect() {
		return xSelect;
	}

	public void setxSelect(Boolean xSelect) {
		this.xSelect = xSelect;
	}
	
}
