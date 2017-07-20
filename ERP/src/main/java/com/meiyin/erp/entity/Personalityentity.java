package com.meiyin.erp.entity;

import android.graphics.drawable.Drawable;



public class Personalityentity {

	Drawable img;
	 String text;
	 
	 
	public Personalityentity(Drawable img, String text) {

		this.img = img;
		this.text = text;
	}
	public Drawable getImg() {
		return img;
	}
	public void setImg(Drawable img) {
		this.img = img;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	 
}
