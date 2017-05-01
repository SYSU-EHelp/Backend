package com.ehelp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 响应类
 */
@Entity
@Table(name="response")
public class Response {

	private int id; // 响应项id
	private int event_type; // 响应事件类型，1代表求助，2代表求救
	private int event_id; // 事件id
	private int user_id; // 响应者id

	public Response() {
	}

	public Response(int event_type, int event_id, int user_id) {
		super();
		this.event_type = event_type;
		this.event_id = event_id;
		this.user_id = user_id;
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

	@Column(name="event_type", nullable = false)
	public int getEvent_type() {
		return event_type;
	}

	public void setEvent_type(int event_type) {
		this.event_type = event_type;
	}

	@Column(name="event_id", nullable = false)
	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

	@Column(name="user_id", nullable = false)
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	@Override
	public String toString() {
		return "Response [id=" + id + ", event_type=" + event_type + ", event_id=" + event_id + ", user_id=" + user_id
				+ "]";
	}

}
