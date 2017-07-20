package com.meiyin.erp.entity;

/**
 * 我的日志
 */
public class MyTaskEntity {

	private String checkS;
	private String weekd;
	private String dtoList;
	private String work_date;
	private String infos;
	private String stafforg;
	private String staffDayReport;
	private String ischeck;

	public MyTaskEntity(String checkS, String weekd, String dtoList,
			String work_date, String infos, String stafforg,
			String staffDayReport, String ischeck) {
		this.checkS = checkS;
		this.weekd = weekd;
		this.dtoList = dtoList;
		this.work_date = work_date;
		this.infos = infos;
		this.stafforg = stafforg;
		this.staffDayReport = staffDayReport;
		this.ischeck = ischeck;
	}
	public String getIscheck() {
		return ischeck;
	}
	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}
	public String getCheckS() {
		return checkS;
	}
	public void setCheckS(String checkS) {
		this.checkS = checkS;
	}
	public String getWeekd() {
		return weekd;
	}
	public void setWeekd(String weekd) {
		this.weekd = weekd;
	}
	public String getDtoList() {
		return dtoList;
	}
	public void setDtoList(String dtoList) {
		this.dtoList = dtoList;
	}
	public String getWork_date() {
		return work_date;
	}
	public void setWork_date(String work_date) {
		this.work_date = work_date;
	}
	public String getInfos() {
		return infos;
	}
	public void setInfos(String infos) {
		this.infos = infos;
	}
	public String getStafforg() {
		return stafforg;
	}
	public void setStafforg(String stafforg) {
		this.stafforg = stafforg;
	}
	public String getStaffDayReport() {
		return staffDayReport;
	}
	public void setStaffDayReport(String staffDayReport) {
		this.staffDayReport = staffDayReport;
	}

}
