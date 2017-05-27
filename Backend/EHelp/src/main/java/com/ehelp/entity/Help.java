package com.ehelp.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 求助类
 */
@Entity
@Table(name = "help")
public class Help {

	private int id; // 求助项id
	private int launcher_id; // 求助者id
	private String title; // 求助题目
	private String description; // 求助内容详情
	private Date date; // 求助日期
	private String address; // 求助者地址
	private double longitude; // 经度
	private double latitude; // 纬度
	private int finished; // 是否求助结束

	public Help() {
	}

	public Help(int launcher_id, String title, String description, Date date, String address, int finished) {
		this.launcher_id = launcher_id;
		this.title = title;
		this.description = description;
		this.date = date;
		this.address = address;
		this.finished = finished;
	}
	
	public Help(int launcher_id, String title, String description, Date date, String address, double longitude,
			double latitude, int finished) {
		this.launcher_id = launcher_id;
		this.title = title;
		this.description = description;
		this.date = date;
		this.address = address;
		this.longitude = longitude;
		this.latitude = latitude;
		this.finished = finished;
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

	@Column(name = "launcher_id", nullable = false)
	public int getLauncher_id() {
		return launcher_id;
	}

	public void setLauncher_id(int launcher_id) {
		this.launcher_id = launcher_id;
	}

	@Column(name = "title", nullable = false, length = 45)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "description", nullable = false, length = 200)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "date", nullable = false)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "address", nullable = false, length = 200)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "longitude", nullable = false)
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Column(name = "latitude", nullable = false)
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Column(name = "finished", nullable = false)
	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	@Override
	public String toString() {
		return "Help [id=" + id + ", launcher_id=" + launcher_id + ", title=" + title + ", description=" + description
				+ ", date=" + date + ", address=" + address + ", longitude=" + longitude + ", latitude=" + latitude
				+ ", finished=" + finished + "]";
	}

}
