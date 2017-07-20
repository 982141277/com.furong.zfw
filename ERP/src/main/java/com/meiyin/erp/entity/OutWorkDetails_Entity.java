package com.meiyin.erp.entity;

import java.io.Serializable;

/**
 * 外出客户地址详情
 */
public class OutWorkDetails_Entity implements Serializable{

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private String ClientId;//客户id
		private String id;//id
		private String apply;//申请单id
		private String targetname; //客户名称
		private String targetid; //targetid
		private String targetaddress; //客户地址
		private String isbrush; //是否刷卡
		private String brushtime; //刷卡时间
		private String brushaddress; //刷卡地址
		private String brush_type;//刷卡类型
		private String sheng;//省
		private String shi;//市
		private String qu;//去

		public OutWorkDetails_Entity(String clientId, String id, String apply,
				String targetname, String targetid, String targetaddress,
				String isbrush, String brushtime, String brushaddress,
				String brush_type, String sheng, String shi, String qu) {
			ClientId = clientId;
			this.id = id;
			this.apply = apply;
			this.targetname = targetname;
			this.targetid = targetid;
			this.targetaddress = targetaddress;
			this.isbrush = isbrush;
			this.brushtime = brushtime;
			this.brushaddress = brushaddress;
			this.brush_type = brush_type;
			this.sheng = sheng;
			this.shi = shi;
			this.qu = qu;
		}
		public String getClientId() {
			return ClientId;
		}
		public void setClientId(String clientId) {
			ClientId = clientId;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getApply() {
			return apply;
		}
		public void setApply(String apply) {
			this.apply = apply;
		}
		public String getTargetname() {
			return targetname;
		}
		public void setTargetname(String targetname) {
			this.targetname = targetname;
		}
		public String getTargetid() {
			return targetid;
		}
		public void setTargetid(String targetid) {
			this.targetid = targetid;
		}
		public String getTargetaddress() {
			return targetaddress;
		}
		public void setTargetaddress(String targetaddress) {
			this.targetaddress = targetaddress;
		}
		public String getIsbrush() {
			return isbrush;
		}
		public void setIsbrush(String isbrush) {
			this.isbrush = isbrush;
		}
		public String getBrushtime() {
			return brushtime;
		}
		public void setBrushtime(String brushtime) {
			this.brushtime = brushtime;
		}
		public String getBrushaddress() {
			return brushaddress;
		}
		public void setBrushaddress(String brushaddress) {
			this.brushaddress = brushaddress;
		}
		public String getBrush_type() {
			return brush_type;
		}
		public void setBrush_type(String brush_type) {
			this.brush_type = brush_type;
		}
		public String getSheng() {
			return sheng;
		}
		public void setSheng(String sheng) {
			this.sheng = sheng;
		}
		public String getShi() {
			return shi;
		}
		public void setShi(String shi) {
			this.shi = shi;
		}
		public String getQu() {
			return qu;
		}
		public void setQu(String qu) {
			this.qu = qu;
		}



	
}
