package com.meiyin.erp.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * ERP通知消息列
 */
@Entity
public class Meiyinnews {
	@Id
	private Long _id;
	private Long time;//时间
	private String id;
	private String name;//标题
	private String biaoshi;//是否操作
	private String type;//类型
	private String username;//用户名
	private String idString;
	public String getIdString() {
		return this.idString;
	}
	public void setIdString(String idString) {
		this.idString = idString;
	}
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBiaoshi() {
		return this.biaoshi;
	}
	public void setBiaoshi(String biaoshi) {
		this.biaoshi = biaoshi;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getTime() {
		return this.time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Long get_id() {
		return this._id;
	}
	public void set_id(Long _id) {
		this._id = _id;
	}
	@Generated(hash = 1904387895)
	public Meiyinnews(Long _id, Long time, String id, String name, String biaoshi,
			String type, String username, String idString) {
		this._id = _id;
		this.time = time;
		this.id = id;
		this.name = name;
		this.biaoshi = biaoshi;
		this.type = type;
		this.username = username;
		this.idString = idString;
	}
	@Generated(hash = 45066757)
	public Meiyinnews() {
	}


	
}
