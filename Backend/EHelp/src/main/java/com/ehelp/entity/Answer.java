package com.ehelp.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 提问回答类
 */
@Entity
@Table(name="answer")
public class Answer {

	private int id; // 答案id
	private int question_id; // 问题id
	private int answerer_id; // 回答者id
	private String description; // 回答内容
	private Date date; // 回答日期

	public Answer() {
	}

	public Answer(int question_id, int answerer_id, String description, Date date) {
		this.question_id = question_id;
		this.answerer_id = answerer_id;
		this.description = description;
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

	@Column(name="question_id", nullable = false)
	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	@Column(name="answerer_id", nullable = false)
	public int getAnswerer_id() {
		return answerer_id;
	}

	public void setAnswerer_id(int answerer_id) {
		this.answerer_id = answerer_id;
	}

	@Column(name="description", nullable = false, length = 200)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		return "Answer [id=" + id + ", question_id=" + question_id + ", answerer_id=" + answerer_id + ", description="
				+ description + ", date=" + date + "]";
	}

}
