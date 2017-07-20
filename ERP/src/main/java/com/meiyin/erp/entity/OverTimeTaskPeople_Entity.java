package com.meiyin.erp.entity;

public class OverTimeTaskPeople_Entity {

	private String id;
	private String currentTime;
	private String mainCause;//审批详情
	private String taskId;
	private String username;
	private String proc_inst_id;
	private String userid;
	private String state;
	private String duTime;
	private String useCheck;
	private String mainContent;//加班事由
	
	public OverTimeTaskPeople_Entity(String id, String currentTime,
			String mainCause, String taskId, String username,
			String proc_inst_id, String userid, String state, String duTime,
			String useCheck, String mainContent) {
		this.id = id;
		this.currentTime = currentTime;
		this.mainCause = mainCause;
		this.taskId = taskId;
		this.username = username;
		this.proc_inst_id = proc_inst_id;
		this.userid = userid;
		this.state = state;
		this.duTime = duTime;
		this.useCheck = useCheck;
		this.mainContent = mainContent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
	public String getMainCause() {
		return mainCause;
	}
	public void setMainCause(String mainCause) {
		this.mainCause = mainCause;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getProc_inst_id() {
		return proc_inst_id;
	}
	public void setProc_inst_id(String proc_inst_id) {
		this.proc_inst_id = proc_inst_id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDuTime() {
		return duTime;
	}
	public void setDuTime(String duTime) {
		this.duTime = duTime;
	}
	public String getUseCheck() {
		return useCheck;
	}
	public void setUseCheck(String useCheck) {
		this.useCheck = useCheck;
	}
	public String getMainContent() {
		return mainContent;
	}
	public void setMainContent(String mainContent) {
		this.mainContent = mainContent;
	}

}
