package com.meiyin.erp.entity;

/**
 * 项目考勤打卡
 */
public class Gooutbrush_Entity {
	
	private String id;
	private String userid;
	private String brushtime;
	private String brushaddress;
	private String longitude;
	private String latitude;
	private String describes;
	private String brushtype;
	
	public Gooutbrush_Entity(String id, String userid, String brushtime,
			String brushaddress, String longitude, String latitude,
			String describes, String brushtype) {
		this.id = id;
		this.userid = userid;
		this.brushtime = brushtime;
		this.brushaddress = brushaddress;
		this.longitude = longitude;
		this.latitude = latitude;
		this.describes = describes;
		this.brushtype = brushtype;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getBrushtime() {
		return brushtime;
	}

	public void setBrushtime(String brushtime) {
		this.brushtime = brushtime;
	}

	public String getBrushaddress() {
		return brushaddress;
	}

	public void setBrushaddress(String brushaddress) {
		this.brushaddress = brushaddress;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getDescribes() {
		return describes;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
	}

	public String getBrushtype() {
		return brushtype;
	}

	public void setBrushtype(String brushtype) {
		this.brushtype = brushtype;
	}

}
