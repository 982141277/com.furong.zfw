package com.meiyin.erp.entity;
/**
 * @param我的任务列表
 * @Time 2016-10-10
 */
public class MyTaskListEntity {
	
	private String task_target;//任务目标
	private String creat_time;//创建时间
	private String task_name;//任务名称
	private String task_id;//任务id
	private String task_status;//任务状态
	private String task_type;//任务类型
	private String creat_username;//任务下达人
	private String operator;//任务执行人
	private String task_notes;//任务描述
	private String source;//任务所需资源
	private String end_time;//任务结束时间
	private String start_time;//任务开始时间
	private String knower;//任务知晓人
	private String creat_user;//任务创建人code
	private String operatorname;
	private String knowername;
	
	public MyTaskListEntity(String task_target, String creat_time,
			String task_name, String task_id, String task_status,
			String task_type, String creat_username, String operator,
			String task_notes, String source, String end_time,
			String start_time, String knower, String creat_user,
			String operatorname, String knowername) {
		this.task_target = task_target;
		this.creat_time = creat_time;
		this.task_name = task_name;
		this.task_id = task_id;
		this.task_status = task_status;
		this.task_type = task_type;
		this.creat_username = creat_username;
		this.operator = operator;
		this.task_notes = task_notes;
		this.source = source;
		this.end_time = end_time;
		this.start_time = start_time;
		this.knower = knower;
		this.creat_user = creat_user;
		this.operatorname = operatorname;
		this.knowername = knowername;
	}
	public String getTask_target() {
		return task_target;
	}
	public void setTask_target(String task_target) {
		this.task_target = task_target;
	}
	public String getCreat_time() {
		return creat_time;
	}
	public void setCreat_time(String creat_time) {
		this.creat_time = creat_time;
	}
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public String getTask_status() {
		return task_status;
	}
	public void setTask_status(String task_status) {
		this.task_status = task_status;
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
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getTask_notes() {
		return task_notes;
	}
	public void setTask_notes(String task_notes) {
		this.task_notes = task_notes;
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
	public String getKnower() {
		return knower;
	}
	public void setKnower(String knower) {
		this.knower = knower;
	}
	public String getCreat_user() {
		return creat_user;
	}
	public void setCreat_user(String creat_user) {
		this.creat_user = creat_user;
	}
	public String getOperatorname() {
		return operatorname;
	}
	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}
	public String getKnowername() {
		return knowername;
	}
	public void setKnowername(String knowername) {
		this.knowername = knowername;
	}
	
//	[{"task_target":"的地方","creat_time":"Oct 10, 2016 9:38:12 AM","task_name":"啊啊啊啊啊",
//		"task_id":6,"task_status":0,"task_type":"1","creat_username":"文总",
//		"operator":[{"relation_id":"615f0a1cc1c5e84cee923b3394e77cc3f5","staff_name":"刘宇庭",
//			"staff_code":"5f0a1cc1c5e84cee923b3394e77cc3f5","task_id":6,"relation_type":1}],
//			"task_notes":"\u003d不不不水电费水电费\u003cbr/\u003e",
//			"source":"都是","end_time":"Oct 20, 2016 12:00:00 AM","start_time":"Oct 10, 2016 12:00:00 AM",
//			"knower":[{"relation_id":"62b7b1a599e24a4c7693933f9a4e01aa37","staff_name":"王顺",
//				"staff_code":"b7b1a599e24a4c7693933f9a4e01aa37","task_id":6,"relation_type":2}],
//				"creat_user":"d1bec532e5224560966be0fb132e8f79"},
}
