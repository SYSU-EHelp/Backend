package com.ehelp.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 提问问题类
 */
@Entity
@Table(name="question")
public class Question {

	private int id; //问题id
	private String title; //问题题目
	private String description; //问题描述
	private int asker_id; //提问者id
	private Date date; //提问日期

	public Question() {
	}

	public Question(String title, String description, int asker_id, Date date) {
		this.title = title;
		this.description = description;
		this.asker_id = asker_id;
		this.date = date;
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

	@Column(name="title", nullable = false, length = 45)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name="description", nullable = false, length = 200)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="asker_id", nullable = false)
	public int getAsker_id() {
		return asker_id;
	}

	public void setAsker_id(int asker_id) {
		this.asker_id = asker_id;
	}

	@Column(name="date", nullable = false)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Question [id=" + id + ", title=" + title + ", description=" + description + ", asker_id=" + asker_id
				+ ", date=" + date + "]";
	}

}
