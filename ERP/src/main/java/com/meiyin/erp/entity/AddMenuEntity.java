package com.meiyin.erp.entity;


/**
 * 新增申请单列表
 */
public class AddMenuEntity {
	
	String field_code;
	String enabled;
	String code_id;
	String is_leaf;
	String code;
	String code_desc;//名称
	String editmode;
	String order_no;
	String parent_id;
	public AddMenuEntity(String field_code, String enabled, String code_id,
			String is_leaf, String code, String code_desc, String editmode,
			String order_no, String parent_id) {
		this.field_code = field_code;
		this.enabled = enabled;
		this.code_id = code_id;
		this.is_leaf = is_leaf;
		this.code = code;
		this.code_desc = code_desc;
		this.editmode = editmode;
		this.order_no = order_no;
		this.parent_id = parent_id;
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
	public String getCode_id() {
		return code_id;
	}
	public void setCode_id(String code_id) {
		this.code_id = code_id;
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
	public String getCode_desc() {
		return code_desc;
	}
	public void setCode_desc(String code_desc) {
		this.code_desc = code_desc;
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
	
	
}
