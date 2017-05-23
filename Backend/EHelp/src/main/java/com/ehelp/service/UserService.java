package com.ehelp.service;

import java.util.List;

import com.ehelp.entity.Contact;
import com.ehelp.entity.User;

public interface UserService {
	
	//根据id获取名字
	public String getName(int id);
	
	//添加验证码
	public boolean addCode(String phone, String code);
	
	//注册添加用户
	public int addUser(String phone, String username, String password, String code);
	
	//检查手机号是否已被注册
	public boolean phoneExisted(String phone, String code);

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
