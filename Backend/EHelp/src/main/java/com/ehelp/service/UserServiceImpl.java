package com.ehelp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehelp.dao.UserDaoImpl;
import com.ehelp.entity.Contact;
import com.ehelp.entity.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDaoImpl userDao;
	
	//注册添加用户
	public boolean addUser(String phone, String username, String password) {
		if (!phoneExisted(phone)) {
			userDao.addUser(username, password, phone, "");
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
	
	//添加紧急联系人
	public boolean addContact(Contact contact) {
		return userDao.addContact(contact);
	}
		
	//获取紧急联系人
	public List<Contact> getContacts(int id) {
		return userDao.getContacts(id);
	}

	//获取我发起的事件
	public List<Object[]> getLaunch(int id) {
		return userDao.getLaunch(id);
	}

	//获取我响应的事件
	public List<Object[]> getResponse(int id) {
		return userDao.getResponse(id);
	}
}




