package com.ehelp.dao;

import com.ehelp.entity.User;

public interface UserDao {

	// 用以注册时检查该手机号是否已被注册
	public boolean phoneExisted(String phone);
	
	// 注册添加用户
	public boolean addUser(String username, String password, String phone, String avatar, String address);
	
	// 用以登录时检查数据库中是否存在该用户
	public int checkUser(User user);
	
	
}
