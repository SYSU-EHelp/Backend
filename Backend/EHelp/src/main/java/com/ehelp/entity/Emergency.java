package com.ehelp.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 求救类
 */
@Entity
@Table(name="emergency")
public class Emergency {

	private int id; // 求救项id
	private int launcher_id; // 求救者id
	private Date date; // 求救日期
	private int finished; // 是否求救结束

	public Emergency() {
	}

	public Emergency(int launcher_id, Date date, int finished) {
		super();
		this.launcher_id = launcher_id;
		this.date = date;
		this.finished = finished;
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

	@Column(name="launcher_id", nullable = false)
	public int getLauncher_id() {
		return launcher_id;
	}

	public void setLauncher_id(int launcher_id) {
		this.launcher_id = launcher_id;
	}

	@Column(name="date", nullable = false)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name="finished", nullable = false)
	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	@Override
	public String toString() {
		return "Emergency [id=" + id + ", launcher_id=" + launcher_id + ", date=" + date + ", finished=" + finished
				+ "]";
	}

}
