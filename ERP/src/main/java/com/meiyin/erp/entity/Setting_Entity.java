package com.meiyin.erp.entity;

import android.graphics.drawable.Drawable;

//系统设置
public class Setting_Entity {

	private Drawable pic;
	private String name;
	public Setting_Entity(Drawable pic, String name) {
		this.pic = pic;
		this.name = name;
	}
	public Drawable getPic() {
		return pic;
	}
	public void setPic(Drawable pic) {
		this.pic = pic;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
