package com.ehelp.entity;

import java.util.Date;

/*
 * 包含提问者信息的问题类
 */
public class QuestionResult {

	private int id; // 问题id
	private String title; // 问题标题
	private String ask_description; // 问题描述
	private Date ask_date; // 提问日期
	private String asker_username; // 提问者用户名
	private String asker_avatar; // 提问者头像路径
	private String answerer_username; // 回答者用户名
	private String answerer_avatar; // 回答者头像路径
	private String answer_description; // 回答内容
	private Date answer_date; // 回答日期

	public QuestionResult() {
	}

	public QuestionResult(int id, String title, String ask_description, Date ask_date, String asker_username,
			String asker_avatar) {
		this.id = id;
		this.title = title;
		this.ask_description = ask_description;
		this.ask_date = ask_date;
		this.asker_username = asker_username;
		this.asker_avatar = asker_avatar;
	}

	public QuestionResult(String answerer_username, String answerer_avatar, String answer_description,
			Date answer_date) {
		this.answerer_username = answerer_username;
		this.answerer_avatar = answerer_avatar;
		this.answer_description = answer_description;
		this.answer_date = answer_date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAsk_description() {
		return ask_description;
	}

	public void setAsk_description(String ask_description) {
		this.ask_description = ask_description;
	}

	public Date getAsk_date() {
		return ask_date;
	}

	public void setAsk_date(Date ask_date) {
		this.ask_date = ask_date;
	}

	public String getAsker_username() {
		return asker_username;
	}

	public void setAsker_username(String asker_username) {
		this.asker_username = asker_username;
	}

	public String getAsker_avatar() {
		return asker_avatar;
	}

	public void setAsker_avatar(String asker_avatar) {
		this.asker_avatar = asker_avatar;
	}

	public String getAnswerer_username() {
		return answerer_username;
	}

	public void setAnswerer_username(String answerer_username) {
		this.answerer_username = answerer_username;
	}

	public String getAnswerer_avatar() {
		return answerer_avatar;
	}

	public void setAnswerer_avatar(String answerer_avatar) {
		this.answerer_avatar = answerer_avatar;
	}

	public String getAnswer_description() {
		return answer_description;
	}

	public void setAnswer_description(String answer_description) {
		this.answer_description = answer_description;
	}

	public Date getAnswer_date() {
		return answer_date;
	}

	public void setAnswer_date(Date answer_date) {
		this.answer_date = answer_date;
	}

	@Override
	public String toString() {
		return "QuestionResult [id=" + id + ", title=" + title + ", ask_description=" + ask_description + ", ask_date="
				+ ask_date + ", asker_username=" + asker_username + ", asker_avatar=" + asker_avatar
				+ ", answerer_username=" + answerer_username + ", answerer_avatar=" + answerer_avatar
				+ ", answer_description=" + answer_description + ", answer_date=" + answer_date + "]";
	}

}
