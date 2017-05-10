package com.ehelp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 用户类
 */
@Entity
@Table(name = "user")
public class User {

	private int id; // 用户id
	private String username; // 用户名
	private String password; // 密码
	private String phone; // 手机号
	private String avatar; // 头像
	private String address; // 地址

	public User() {
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public User(String username, String password, String phone, String avatar, String address) {
		this.username = username;
		this.password = password;
		this.phone = phone;
		this.avatar = avatar;
		this.address = address;
	}

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "username", unique = true, nullable = false, length = 45)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", nullable = false, length = 45)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "phone", unique = true, nullable = false, length = 15)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "avatar", length = 45)
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Column(name = "address", length = 200)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", phone=" + phone + ", avatar="
				+ avatar + ", address=" + address + "]";
	}

}
