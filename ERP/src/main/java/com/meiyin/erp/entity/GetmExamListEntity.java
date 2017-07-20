package com.meiyin.erp.entity;

public class GetmExamListEntity {
	protected String attr_id;
	protected String bz;
	protected String display_type;
	protected String attr_name;
	protected String normal_range;
	protected String attr_value;

	public GetmExamListEntity(String attr_id, String bz, String display_type,
			String attr_name, String normal_range, String attr_value) {
		this.attr_id = attr_id;
		this.bz = bz;
		this.display_type = display_type;
		this.attr_name = attr_name;
		this.normal_range = normal_range;
		this.attr_value = attr_value;
	}

	public String getAttr_id() {
		return attr_id;
	}

	public void setAttr_id(String attr_id) {
		this.attr_id = attr_id;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getDisplay_type() {
		return display_type;
	}

	public void setDisplay_type(String display_type) {
		this.display_type = display_type;
	}

	public String getAttr_name() {
		return attr_name;
	}

	public void setAttr_name(String attr_name) {
		this.attr_name = attr_name;
	}

	public String getNormal_range() {
		return normal_range;
	}

	public void setNormal_range(String normal_range) {
		this.normal_range = normal_range;
	}

	public String getAttr_value() {
		return attr_value;
	}

	public void setAttr_value(String attr_value) {
		this.attr_value = attr_value;
	}
}
