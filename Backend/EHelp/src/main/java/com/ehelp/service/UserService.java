package com.ehelp.service;

import java.util.List;

import com.ehelp.entity.Contact;
import com.ehelp.entity.User;

public interface UserService {
	
	//注册添加用户
	public boolean addUser(String phone, String username, String password);
	
	//检查手机号是否已被注册
	public boolean phoneExisted(String phone);
	
	//登录
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
