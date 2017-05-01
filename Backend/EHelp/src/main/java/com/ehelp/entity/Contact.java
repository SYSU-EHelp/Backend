package com.ehelp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 紧急联系人类
 */
@Entity
@Table(name="contact")
public class Contact {

	private int id; // 联系项id
	private int user_id; // 用户id
	private String contact_user; // 紧急联系人名字
	private String contact_phone; // 紧急联系人手机号

	public Contact() {
	}

	public Contact(int user_id, String contact_user, String contact_phone) {
		super();
		this.user_id = user_id;
		this.contact_user = contact_user;
		this.contact_phone = contact_phone;
	}

	@Id
	@Column(name="id", nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name="user_id", nullable = false)
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	@Column(name="contact_user", nullable = false, length = 45)
	public String getContact_user() {
		return contact_user;
	}

	public void setContact_user(String contact_user) {
		this.contact_user = contact_user;
	}

	@Column(name="contact_phone", nullable = false, length = 45)
	public String getContact_phone() {
		return contact_phone;
	}

	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", user_id=" + user_id + ", contact_user=" + contact_user + ", contact_phone="
				+ contact_phone + "]";
	}

}
