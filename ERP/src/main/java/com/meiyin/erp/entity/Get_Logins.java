package com.meiyin.erp.entity;
/**
 * ERP登陆
 */
public class Get_Logins {
	private	String system_code;
	private String system_name;
	private Long size;
	public String getSystem_code() {
		return system_code;
	}
	public void setSystem_code(String system_code) {
		this.system_code = system_code;
	}
	public String getSystem_name() {
		return system_name;
	}
	public void setSystem_name(String system_name) {
		this.system_name = system_name;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public Get_Logins(String system_code, String system_name, Long size) {
		this.system_code = system_code;
		this.system_name = system_name;
		this.size = size;
	}
	
	
}
