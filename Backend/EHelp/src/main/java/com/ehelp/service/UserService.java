package com.ehelp.service;

import com.ehelp.entity.User;

public interface UserService {
	
	//注册添加用户
	public Boolean addUser(String phone, String username, String password);
	
	//检查手机号是否已被注册
	public boolean phoneExisted(String phone);
	
	//登录
	public int checkUser(User user);
	
}
