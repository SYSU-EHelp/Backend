package com.ehelp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehelp.dao.UserDaoImpl;
import com.ehelp.entity.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDaoImpl userDao;
	
	//注册添加用户
	public Boolean addUser(String phone, String username, String password) {
		if (!phoneExisted(phone)) {
			userDao.addUser(username, password, phone, "", "");
			return true;
		}
		return false;
	}
	
	//检查手机号是否已被注册
	public boolean phoneExisted(String phone) {
		return userDao.phoneExisted(phone);
	}
	
	//登录
	public int checkUser(User user) {
		return userDao.checkUser(user);
	}
}
