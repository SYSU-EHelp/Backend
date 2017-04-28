package com.ehelp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehelp.dao.UserDao;
import com.ehelp.entity.User;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	public Boolean addUser(String phone, String username, String password) {
		if (checkUser(phone)) {
			userDao.addUser(phone, username, password);
			return true;
		}
		return false;
	}
	
	public boolean checkUser(String phone) {
		return userDao.checkUser(phone);
	}
	
	public boolean checkUser2(User user) {
		return userDao.checkUser2(user);
	}
}
