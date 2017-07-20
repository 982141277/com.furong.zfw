package com.meiyin.erp.entity;

public class ClientInfoList {

	
	String client_name;
	String client_address;
	String detail;
	public ClientInfoList(String client_name, String client_address,
			String detail) {
		this.client_name = client_name;
		this.client_address = client_address;
		this.detail = detail;
	}
	public String getClient_name() {
		return client_name;
	}
	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}
	public String getClient_address() {
		return client_address;
	}
	public void setClient_address(String client_address) {
		this.client_address = client_address;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
}
