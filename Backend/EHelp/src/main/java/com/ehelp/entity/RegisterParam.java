package com.ehelp.entity;

public class RegisterParam {

	private String phone;
	private String username;
	private String password;
	private String code;

	public RegisterParam() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RegisterParam(String phone, String username, String password, String code) {
		super();
		this.phone = phone;
		this.username = username;
		this.password = password;
		this.code = code;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
