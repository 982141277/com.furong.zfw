package com.meiyin.erp.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ClientAddress_Entity {
	@Id
	private Long _id;
	private String id;
	private String sheng;
	private String shi;
	private String qu;
	public String getQu() {
		return this.qu;
	}
	public void setQu(String qu) {
		this.qu = qu;
	}
	public String getShi() {
		return this.shi;
	}
	public void setShi(String shi) {
		this.shi = shi;
	}
	public String getSheng() {
		return this.sheng;
	}
	public void setSheng(String sheng) {
		this.sheng = sheng;
	}
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long get_id() {
		return this._id;
	}
	public void set_id(Long _id) {
		this._id = _id;
	}
	@Generated(hash = 864975654)
	public ClientAddress_Entity(Long _id, String id, String sheng, String shi,
			String qu) {
		this._id = _id;
		this.id = id;
		this.sheng = sheng;
		this.shi = shi;
		this.qu = qu;
	}
	@Generated(hash = 426877172)
	public ClientAddress_Entity() {
	}


	
}
