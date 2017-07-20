package com.meiyin.erp.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 申请单列表
 */
@Entity
public class Memulist_Entity {
	@Id
	private Long _id;
	private String type; //申请单类型
	private String applicant; //申请人
	private String section;//所属部门
	private String applycause;//申请原因
	private String flowstate; //流程状态
	private String flowcaption; //流程说明
	private Long createTime;//创建时间
	private String procInstId;//procinstId
	private String applyNameState;//申请类型id
	private String applyId;//主键id
	private String descId;//descId
	private String taskId;//taskId
	private String mainContent;//外出内容
	private String leave_reason;//请假原因
	private String applyDesc;//费用原因
	private String reason;//请购原因
	public String getReason() {
		return this.reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getApplyDesc() {
		return this.applyDesc;
	}
	public void setApplyDesc(String applyDesc) {
		this.applyDesc = applyDesc;
	}
	public String getLeave_reason() {
		return this.leave_reason;
	}
	public void setLeave_reason(String leave_reason) {
		this.leave_reason = leave_reason;
	}
	public String getMainContent() {
		return this.mainContent;
	}
	public void setMainContent(String mainContent) {
		this.mainContent = mainContent;
	}
	public String getTaskId() {
		return this.taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getDescId() {
		return this.descId;
	}
	public void setDescId(String descId) {
		this.descId = descId;
	}
	public String getApplyId() {
		return this.applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	public String getApplyNameState() {
		return this.applyNameState;
	}
	public void setApplyNameState(String applyNameState) {
		this.applyNameState = applyNameState;
	}
	public String getProcInstId() {
		return this.procInstId;
	}
	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}
	public Long getCreateTime() {
		return this.createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public String getFlowcaption() {
		return this.flowcaption;
	}
	public void setFlowcaption(String flowcaption) {
		this.flowcaption = flowcaption;
	}
	public String getFlowstate() {
		return this.flowstate;
	}
	public void setFlowstate(String flowstate) {
		this.flowstate = flowstate;
	}
	public String getApplycause() {
		return this.applycause;
	}
	public void setApplycause(String applycause) {
		this.applycause = applycause;
	}
	public String getSection() {
		return this.section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getApplicant() {
		return this.applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long get_id() {
		return this._id;
	}
	public void set_id(Long _id) {
		this._id = _id;
	}
	@Generated(hash = 116234944)
	public Memulist_Entity(Long _id, String type, String applicant, String section,
			String applycause, String flowstate, String flowcaption, Long createTime,
			String procInstId, String applyNameState, String applyId, String descId,
			String taskId, String mainContent, String leave_reason, String applyDesc,
			String reason) {
		this._id = _id;
		this.type = type;
		this.applicant = applicant;
		this.section = section;
		this.applycause = applycause;
		this.flowstate = flowstate;
		this.flowcaption = flowcaption;
		this.createTime = createTime;
		this.procInstId = procInstId;
		this.applyNameState = applyNameState;
		this.applyId = applyId;
		this.descId = descId;
		this.taskId = taskId;
		this.mainContent = mainContent;
		this.leave_reason = leave_reason;
		this.applyDesc = applyDesc;
		this.reason = reason;
	}
	@Generated(hash = 340082184)
	public Memulist_Entity() {
	}






}
