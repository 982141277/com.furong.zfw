package com.meiyin.erp.entity;

/**
 * @param请购单物品实体类
 * @Time 2016-5-12
 */
public class Requisition_Entity {
	private String accompanyId2;
	private String apply_state;//审批流程吧。。。
	private String remark;//备注
	private String prod_id;
	private String prod_Model;//产品型号
	private String userid;
	private String need_num_old;
	private String need_num;//需要数量
	private String unit_price;//参考单价
	private String url;//网店参考链接
	private String accompanyId;
	
	private String orgId;
	private String prod_unit;//单位
	private String prod_name_0;//物品名称
	private String projectName;//项目名称
	private String purApplyId;
	public Requisition_Entity(String accompanyId2, String apply_state,
			String remark, String prod_id, String prod_Model, String userid,
			String need_num_old, String need_num, String unit_price,
			String url, String accompanyId, String orgId, String prod_unit,
			String prod_name_0, String projectName, String purApplyId) {
		this.accompanyId2 = accompanyId2;
		this.apply_state = apply_state;
		this.remark = remark;
		this.prod_id = prod_id;
		this.prod_Model = prod_Model;
		this.userid = userid;
		this.need_num_old = need_num_old;
		this.need_num = need_num;
		this.unit_price = unit_price;
		this.url = url;
		this.accompanyId = accompanyId;
		this.orgId = orgId;
		this.prod_unit = prod_unit;
		this.prod_name_0 = prod_name_0;
		this.projectName = projectName;
		this.purApplyId = purApplyId;
	}
	public String getAccompanyId2() {
		return accompanyId2;
	}
	public void setAccompanyId2(String accompanyId2) {
		this.accompanyId2 = accompanyId2;
	}
	public String getApply_state() {
		return apply_state;
	}
	public void setApply_state(String apply_state) {
		this.apply_state = apply_state;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getProd_id() {
		return prod_id;
	}
	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}
	public String getProd_Model() {
		return prod_Model;
	}
	public void setProd_Model(String prod_Model) {
		this.prod_Model = prod_Model;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getNeed_num_old() {
		return need_num_old;
	}
	public void setNeed_num_old(String need_num_old) {
		this.need_num_old = need_num_old;
	}
	public String getNeed_num() {
		return need_num;
	}
	public void setNeed_num(String need_num) {
		this.need_num = need_num;
	}
	public String getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAccompanyId() {
		return accompanyId;
	}
	public void setAccompanyId(String accompanyId) {
		this.accompanyId = accompanyId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getProd_unit() {
		return prod_unit;
	}
	public void setProd_unit(String prod_unit) {
		this.prod_unit = prod_unit;
	}
	public String getProd_name_0() {
		return prod_name_0;
	}
	public void setProd_name_0(String prod_name_0) {
		this.prod_name_0 = prod_name_0;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getPurApplyId() {
		return purApplyId;
	}
	public void setPurApplyId(String purApplyId) {
		this.purApplyId = purApplyId;
	}
	
}
