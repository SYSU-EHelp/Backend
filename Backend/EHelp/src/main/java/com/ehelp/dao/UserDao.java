package com.ehelp.dao;

import java.util.List;

import com.ehelp.entity.Contact;
import com.ehelp.entity.User;

public interface UserDao {

	// 用以注册时检查该手机号是否已被注册
	public boolean phoneExisted(String phone);
	
	// 注册添加用户
	public boolean addUser(String username, String password, String phone, String avatar);
	
	// 用以登录时检查数据库中是否存在该用户
	public int checkUser(User user);
	
	//添加紧急联系人
	public boolean addContact(Contact contact);
			
	//获取紧急联系人
	public List<Contact> getContacts(int id);
	
	//获取我发起的事件
	public List<Object[]> getLaunch(int id);
	
	//获取我响应的事件
	public List<Object[]> getResponse(int id);
	
}
