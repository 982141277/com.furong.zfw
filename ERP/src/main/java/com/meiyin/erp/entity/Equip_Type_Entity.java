package com.meiyin.erp.entity;
/*
 * 事件类型
 */
public class Equip_Type_Entity {
	
	private String field_code;//EVENTTYPE
	private String enabled;//1
	private String is_leaf;//1
	private String code;//"614"
	private String editmode;//1
	private String order_no;//1
	private String parent_id;//父id**"44338453"
	
	private String code_desc;//内容**"内存故障"
	private String code_id; //id**"46985847"

	public Equip_Type_Entity(String field_code, String enabled, String is_leaf,
			String code, String editmode, String order_no, String parent_id,
			String code_desc, String code_id) {
		this.field_code = field_code;
		this.enabled = enabled;
		this.is_leaf = is_leaf;
		this.code = code;
		this.editmode = editmode;
		this.order_no = order_no;
		this.parent_id = parent_id;
		this.code_desc = code_desc;
		this.code_id = code_id;
	}
	
	public String getField_code() {
		return field_code;
	}

	public void setField_code(String field_code) {
		this.field_code = field_code;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getIs_leaf() {
		return is_leaf;
	}

	public void setIs_leaf(String is_leaf) {
		this.is_leaf = is_leaf;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEditmode() {
		return editmode;
	}

	public void setEditmode(String editmode) {
		this.editmode = editmode;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getCode_desc() {
		return code_desc;
	}
	public void setCode_desc(String code_desc) {
		this.code_desc = code_desc;
	}
	public String getCode_id() {
		return code_id;
	}
	public void setCode_id(String code_id) {
		this.code_id = code_id;
	}
	
}
