package com.meiyin.erp.entity;

public class OutWork_Entity {
	
	String _id;//主键id
	String userid; //用户id
	String outtype; //外出类型
	String applicant; //申请人
	String section;//所属部门
	long time;//创建时间
	String brushstring; //是否刷卡
	String toLocation;//目的地
	String mainContent ;//主要内容
	
	public OutWork_Entity(String _id, String userid, String outtype,
			String applicant, String section, long time, String brushstring,
			String toLocation, String mainContent) {
		this._id = _id;
		this.userid = userid;
		this.outtype = outtype;
		this.applicant = applicant;
		this.section = section;
		this.time = time;
		this.brushstring = brushstring;
		this.toLocation = toLocation;
		this.mainContent = mainContent;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getOuttype() {
		return outtype;
	}
	public void setOuttype(String outtype) {
		this.outtype = outtype;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getBrushstring() {
		return brushstring;
	}
	public void setBrushstring(String brushstring) {
		this.brushstring = brushstring;
	}
	public String getToLocation() {
		return toLocation;
	}
	public void setToLocation(String toLocation) {
		this.toLocation = toLocation;
	}
	public String getMainContent() {
		return mainContent;
	}
	public void setMainContent(String mainContent) {
		this.mainContent = mainContent;
	}


}
