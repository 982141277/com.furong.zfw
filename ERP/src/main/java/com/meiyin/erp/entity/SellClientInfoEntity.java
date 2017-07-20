package com.meiyin.erp.entity;

import java.io.Serializable;

/**
 * @param销售系统客户资料实体类
 * @Time 2016-5-19
 */
public class SellClientInfoEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sClientTypeID;
	private String isDeleteClient;
	private String sBusinessTypeID;
	private String sClientWorkAddr;//客户地址
	private String sClientSimpleName;//客户简称
	private String fID;
	private String sIndustryID;
	private String sClientStaffCount;//人员数量
	private String sClientName;//客户名称
	private String sClientSource;//客户资源
	private String sClientType;//客户类型（重点客户）
	private String sIndustry;//客户所属行业
	private String sWriteDate;//时间
	private String sClientState;//客户单位基本情况
	private String sClientSourceID;
	private String sClientAreaID;
	private String sPersonID;
	private String sWriteStaff;
	private boolean isChecked;//是否选中
	
	public SellClientInfoEntity(String sClientTypeID, String isDeleteClient,
			String sBusinessTypeID, String sClientWorkAddr,
			String sClientSimpleName, String fID, String sIndustryID,
			String sClientStaffCount, String sClientName, String sClientSource,
			String sClientType, String sIndustry, String sWriteDate,
			String sClientState, String sClientSourceID, String sClientAreaID,
			String sPersonID, String sWriteStaff, boolean isChecked) {
		this.sClientTypeID = sClientTypeID;
		this.isDeleteClient = isDeleteClient;
		this.sBusinessTypeID = sBusinessTypeID;
		this.sClientWorkAddr = sClientWorkAddr;
		this.sClientSimpleName = sClientSimpleName;
		this.fID = fID;
		this.sIndustryID = sIndustryID;
		this.sClientStaffCount = sClientStaffCount;
		this.sClientName = sClientName;
		this.sClientSource = sClientSource;
		this.sClientType = sClientType;
		this.sIndustry = sIndustry;
		this.sWriteDate = sWriteDate;
		this.sClientState = sClientState;
		this.sClientSourceID = sClientSourceID;
		this.sClientAreaID = sClientAreaID;
		this.sPersonID = sPersonID;
		this.sWriteStaff = sWriteStaff;
		this.isChecked = isChecked;
	}

	public boolean getIsChecked() {
		return isChecked;
	}

	public boolean setIsChecked(boolean isChecked) {
		return this.isChecked = isChecked;
	}

	public String getsClientTypeID() {
		return sClientTypeID;
	}

	public void setsClientTypeID(String sClientTypeID) {
		this.sClientTypeID = sClientTypeID;
	}

	public String getIsDeleteClient() {
		return isDeleteClient;
	}

	public void setIsDeleteClient(String isDeleteClient) {
		this.isDeleteClient = isDeleteClient;
	}

	public String getsBusinessTypeID() {
		return sBusinessTypeID;
	}

	public void setsBusinessTypeID(String sBusinessTypeID) {
		this.sBusinessTypeID = sBusinessTypeID;
	}

	public String getsClientWorkAddr() {
		return sClientWorkAddr;
	}

	public void setsClientWorkAddr(String sClientWorkAddr) {
		this.sClientWorkAddr = sClientWorkAddr;
	}

	public String getsClientSimpleName() {
		return sClientSimpleName;
	}

	public void setsClientSimpleName(String sClientSimpleName) {
		this.sClientSimpleName = sClientSimpleName;
	}

	public String getfID() {
		return fID;
	}

	public void setfID(String fID) {
		this.fID = fID;
	}

	public String getsIndustryID() {
		return sIndustryID;
	}

	public void setsIndustryID(String sIndustryID) {
		this.sIndustryID = sIndustryID;
	}

	public String getsClientStaffCount() {
		return sClientStaffCount;
	}

	public void setsClientStaffCount(String sClientStaffCount) {
		this.sClientStaffCount = sClientStaffCount;
	}

	public String getsClientName() {
		return sClientName;
	}

	public void setsClientName(String sClientName) {
		this.sClientName = sClientName;
	}

	public String getsClientSource() {
		return sClientSource;
	}

	public void setsClientSource(String sClientSource) {
		this.sClientSource = sClientSource;
	}

	public String getsClientType() {
		return sClientType;
	}

	public void setsClientType(String sClientType) {
		this.sClientType = sClientType;
	}

	public String getsIndustry() {
		return sIndustry;
	}

	public void setsIndustry(String sIndustry) {
		this.sIndustry = sIndustry;
	}

	public String getsWriteDate() {
		return sWriteDate;
	}

	public void setsWriteDate(String sWriteDate) {
		this.sWriteDate = sWriteDate;
	}

	public String getsClientState() {
		return sClientState;
	}

	public void setsClientState(String sClientState) {
		this.sClientState = sClientState;
	}

	public String getsClientSourceID() {
		return sClientSourceID;
	}

	public void setsClientSourceID(String sClientSourceID) {
		this.sClientSourceID = sClientSourceID;
	}

	public String getsClientAreaID() {
		return sClientAreaID;
	}

	public void setsClientAreaID(String sClientAreaID) {
		this.sClientAreaID = sClientAreaID;
	}

	public String getsPersonID() {
		return sPersonID;
	}

	public void setsPersonID(String sPersonID) {
		this.sPersonID = sPersonID;
	}

	public String getsWriteStaff() {
		return sWriteStaff;
	}

	public void setsWriteStaff(String sWriteStaff) {
		this.sWriteStaff = sWriteStaff;
	}

	

}
