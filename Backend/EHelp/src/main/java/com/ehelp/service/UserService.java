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
		if (phoneExisted(phone)) {
			userDao.addUser(username, password, phone, "", "");
			return true;
		}
		return false;
	}
	
	public boolean phoneExisted(String phone) {
		return userDao.phoneExisted(phone);
	}
	
	public boolean checkUser(User user) {
		return userDao.checkUser(user);
	}
}
