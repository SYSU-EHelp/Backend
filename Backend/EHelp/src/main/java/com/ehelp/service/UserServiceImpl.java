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
	
	public String getName(int id) {
		return userDao.getName(id);
	}
	
	//添加验证码
	public boolean addCode(String phone, String code) {
		return userDao.addCode(phone, code);
	}
	
	//注册添加用户
	public int addUser(String phone, String username, String password, String code) {
		if (!phoneExisted(phone, code)) {
			int status = userDao.addUser(username, password, phone, "", code);
			if (status == 0) return 0; //用户名已存在
			if (status == 1) return 1; //成功
			else return 3; //验证码错误
		}
		return 2; //手机已被注册
	}
	
	//检查手机号是否已被注册
	public boolean phoneExisted(String phone, String code) {
		return userDao.phoneExisted(phone, code);
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




