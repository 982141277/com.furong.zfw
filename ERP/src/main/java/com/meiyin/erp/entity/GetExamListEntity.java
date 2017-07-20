package com.meiyin.erp.entity;

import java.util.ArrayList;



public class GetExamListEntity {
	protected ArrayList<GetmExamListEntity> equipExamList;
	protected String equip_id;

	public GetExamListEntity(ArrayList<GetmExamListEntity> equipExamList, String equip_id) {
		this.equipExamList = equipExamList;
		this.equip_id = equip_id;
	}

	public ArrayList<GetmExamListEntity> getEquipExamList() {
		return equipExamList;
	}

	public void setEquipExamList(ArrayList<GetmExamListEntity> equipExamList) {
		this.equipExamList = equipExamList;
	}

	public String getEquip_id() {
		return equip_id;
	}

	public void setEquip_id(String equip_id) {
		this.equip_id = equip_id;
	}

}
