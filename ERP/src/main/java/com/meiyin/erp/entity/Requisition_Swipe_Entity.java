package com.meiyin.erp.entity;

import java.io.Serializable;

/**
 * @param请购单物品实体类2
 * @Time 2016-5-27
 */
public class Requisition_Swipe_Entity implements Serializable{

	private static final long serialVersionUID = 1L;
		private String orgId;//物品名称id	
		private String org_name;//物品名称
		private String accompanyId;//产品类型id
		private String accompany_name;//产品类型名称
		private String accompanyId2;//产品品牌id
		private String accompanyId2_name;//产品品牌名称
		private String prod_Model;//产品型号
		private String need_num;//需要数量
		private String prod_unit;//单位
		private String unit_price;//参考单价
		private String url;//网店参考链接
		private String remark;//备注
		private int index1;
		private int index2;
		private int index22;
		private int index3;
		private int index4;


		public Requisition_Swipe_Entity(String orgId, String org_name,
				String accompanyId, String accompany_name, String accompanyId2,
				String accompanyId2_name, String prod_Model, String need_num,
				String prod_unit, String unit_price, String url, String remark,
				int index1, int index2, int index22, int index3, int index4) {
			this.orgId = orgId;
			this.org_name = org_name;
			this.accompanyId = accompanyId;
			this.accompany_name = accompany_name;
			this.accompanyId2 = accompanyId2;
			this.accompanyId2_name = accompanyId2_name;
			this.prod_Model = prod_Model;
			this.need_num = need_num;
			this.prod_unit = prod_unit;
			this.unit_price = unit_price;
			this.url = url;
			this.remark = remark;
			this.index1 = index1;
			this.index2 = index2;
			this.index22 = index22;
			this.index3 = index3;
			this.index4 = index4;
		}


		public int getIndex1() {
			return index1;
		}


		public void setIndex1(int index1) {
			this.index1 = index1;
		}


		public int getIndex2() {
			return index2;
		}


		public void setIndex2(int index2) {
			this.index2 = index2;
		}


		public int getIndex22() {
			return index22;
		}


		public void setIndex22(int index22) {
			this.index22 = index22;
		}


		public int getIndex3() {
			return index3;
		}


		public void setIndex3(int index3) {
			this.index3 = index3;
		}


		public int getIndex4() {
			return index4;
		}


		public void setIndex4(int index4) {
			this.index4 = index4;
		}


		public String getOrg_name() {
			return org_name;
		}

		public void setOrg_name(String org_name) {
			this.org_name = org_name;
		}

		public String getAccompany_name() {
			return accompany_name;
		}

		public void setAccompany_name(String accompany_name) {
			this.accompany_name = accompany_name;
		}

		public String getAccompanyId2_name() {
			return accompanyId2_name;
		}

		public void setAccompanyId2_name(String accompanyId2_name) {
			this.accompanyId2_name = accompanyId2_name;
		}

		public String getOrgId() {
			return orgId;
		}

		public void setOrgId(String orgId) {
			this.orgId = orgId;
		}

		public String getAccompanyId() {
			return accompanyId;
		}

		public void setAccompanyId(String accompanyId) {
			this.accompanyId = accompanyId;
		}

		public String getAccompanyId2() {
			return accompanyId2;
		}

		public void setAccompanyId2(String accompanyId2) {
			this.accompanyId2 = accompanyId2;
		}

		public String getProd_Model() {
			return prod_Model;
		}

		public void setProd_Model(String prod_Model) {
			this.prod_Model = prod_Model;
		}

		public String getNeed_num() {
			return need_num;
		}

		public void setNeed_num(String need_num) {
			this.need_num = need_num;
		}

		public String getProd_unit() {
			return prod_unit;
		}

		public void setProd_unit(String prod_unit) {
			this.prod_unit = prod_unit;
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

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}


}
