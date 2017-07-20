package com.meiyin.erp.entity;
/**
 * 我的日志子列表
 */
public class MymTaskEntity {
	private String task_target;
	private String task_name;
	private String name;
	private String status;
	private String creat_time;
	private String task_status;
	private String task_id;
	private String task_type;
	private String creat_username;
	private String relation_type;
	private String type;
	private String id;
	private String time;
	private String source;
	private String end_time;
	private String start_time;
	private String notes;
	private String verybusy;
	private String reportId;


	public MymTaskEntity(String task_target, String task_name, String name,
			String status, String creat_time, String task_status,
			String task_id, String task_type, String creat_username,
			String relation_type, String type, String id, String time,
			String source, String end_time, String start_time, String notes,
			String verybusy, String reportId) {
		this.task_target = task_target;
		this.task_name = task_name;
		this.name = name;
		this.status = status;
		this.creat_time = creat_time;
		this.task_status = task_status;
		this.task_id = task_id;
		this.task_type = task_type;
		this.creat_username = creat_username;
		this.relation_type = relation_type;
		this.type = type;
		this.id = id;
		this.time = time;
		this.source = source;
		this.end_time = end_time;
		this.start_time = start_time;
		this.notes = notes;
		this.verybusy = verybusy;
		this.reportId = reportId;
	}
	public String getTask_target() {
		return task_target;
	}
	public void setTask_target(String task_target) {
		this.task_target = task_target;
	}
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreat_time() {
		return creat_time;
	}
	public void setCreat_time(String creat_time) {
		this.creat_time = creat_time;
	}
	public String getTask_status() {
		return task_status;
	}
	public void setTask_status(String task_status) {
		this.task_status = task_status;
	}
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public String getTask_type() {
		return task_type;
	}
	public void setTask_type(String task_type) {
		this.task_type = task_type;
	}
	public String getCreat_username() {
		return creat_username;
	}
	public void setCreat_username(String creat_username) {
		this.creat_username = creat_username;
	}
	public String getRelation_type() {
		return relation_type;
	}
	public void setRelation_type(String relation_type) {
		this.relation_type = relation_type;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getVerybusy() {
		return verybusy;
	}
	public void setVerybusy(String verybusy) {
		this.verybusy = verybusy;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

}
