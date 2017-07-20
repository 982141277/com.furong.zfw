package com.meiyin.erp.entity;

import java.util.ArrayList;



public class ExamListEntity {
	protected ArrayList<ExammListEntity> equipExamList;
	protected String model_num;
	protected String type_name;
	protected String eq_explain;
	protected String equip_state;
	protected String euip_index;
	protected String type_id;
	protected String equip_id;
	protected String order_id;
	protected String check_period;
	protected String brand_name;
	protected String brand_code;
	protected String equipExamListSize;
	protected String DPFWQ_IP_ETH1371;

	public ExamListEntity(ArrayList<ExammListEntity> equipExamList, String model_num,
			String type_name, String eq_explain, String equip_state,
			String euip_index, String type_id, String equip_id,
			String order_id, String check_period, String brand_name,
			String brand_code, String equipExamListSize,
			String dPFWQ_IP_ETH1371) {
		this.equipExamList = equipExamList;
		this.model_num = model_num;
		this.type_name = type_name;
		this.eq_explain = eq_explain;
		this.equip_state = equip_state;
		this.euip_index = euip_index;
		this.type_id = type_id;
		this.equip_id = equip_id;
		this.order_id = order_id;
		this.check_period = check_period;
		this.brand_name = brand_name;
		this.brand_code = brand_code;
		this.equipExamListSize = equipExamListSize;
		DPFWQ_IP_ETH1371 = dPFWQ_IP_ETH1371;
	}

	public ArrayList<ExammListEntity> getEquipExamList() {
		return equipExamList;
	}

	public void setEquipExamList(ArrayList<ExammListEntity> equipExamList) {
		this.equipExamList = equipExamList;
	}

	public String getModel_num() {
		return model_num;
	}

	public void setModel_num(String model_num) {
		this.model_num = model_num;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getEq_explain() {
		return eq_explain;
	}

	public void setEq_explain(String eq_explain) {
		this.eq_explain = eq_explain;
	}

	public String getEquip_state() {
		return equip_state;
	}

	public void setEquip_state(String equip_state) {
		this.equip_state = equip_state;
	}

	public String getEuip_index() {
		return euip_index;
	}

	public void setEuip_index(String euip_index) {
		this.euip_index = euip_index;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getEquip_id() {
		return equip_id;
	}

	public void setEquip_id(String equip_id) {
		this.equip_id = equip_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getCheck_period() {
		return check_period;
	}

	public void setCheck_period(String check_period) {
		this.check_period = check_period;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getBrand_code() {
		return brand_code;
	}

	public void setBrand_code(String brand_code) {
		this.brand_code = brand_code;
	}

	public String getEquipExamListSize() {
		return equipExamListSize;
	}

	public void setEquipExamListSize(String equipExamListSize) {
		this.equipExamListSize = equipExamListSize;
	}

	public String getDPFWQ_IP_ETH1371() {
		return DPFWQ_IP_ETH1371;
	}

	public void setDPFWQ_IP_ETH1371(String dPFWQ_IP_ETH1371) {
		DPFWQ_IP_ETH1371 = dPFWQ_IP_ETH1371;
	}

}