package com.ehelp.dao;

import java.util.List;

import com.ehelp.entity.Contact;
import com.ehelp.entity.User;

public interface UserDao {
	
	//根据id获取名字
	public String getName(int id);
	
	//根据id获取用户
	public User getUser(int id);

	//添加验证码
	public boolean addCode(String phone, String code);
	
	// 用以注册时检查该手机号是否已被使用
	public boolean phoneExisted(String phone, String code);
	
	// 注册添加用户
	public int addUser(String username, String password, String phone, String avatar, String code);
	
	// 用以登录时检查数据库中是否存在该用户
	public int checkUser(User user);
	
	//添加紧急联系人
	public int addContact(Contact contact);
	
	//删除紧急联系人
	public boolean deleteContact(int user_id, String username);
	
	//获取紧急联系人
	public List<Contact> getContacts(int id);
	
	//获取我发起的事件
	public List<Object[]> getLaunch(int id);
	
	//获取我响应的事件
	public List<Object[]> getResponse(int id);
	
}
