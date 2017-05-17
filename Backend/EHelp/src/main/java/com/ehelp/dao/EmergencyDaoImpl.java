package com.ehelp.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ehelp.entity.Emergency;
import com.ehelp.entity.User;
import com.ehelp.util.DBSessionUtil;

@Repository
public class EmergencyDaoImpl implements EmergencyDao {

	//发起求救
	public boolean launchEmergency(Emergency e) {
		Session session = DBSessionUtil.getSession();
		session.save(e);
		DBSessionUtil.closeSession(session);
		return true;
	}

	public List<String> getPhones(int id) {
		List<String> phoneList = new ArrayList<String>();
		Session session = DBSessionUtil.getSession();
		// 查询语句
		Query query = session.createQuery("select c.contact_phone "
				+ "from User u, Contact c where u.id=:id and u.id=c.user_id");
		query.setParameter("id", id);
		// 查询结果
		phoneList = query.list();
		// 事务提交并关闭
		DBSessionUtil.closeSession(session);
		return phoneList;
	}
	
	public String getName(int id) {
		Session session = DBSessionUtil.getSession();
		User u = (User) session.get(User.class, id);
		DBSessionUtil.closeSession(session);
		return u.getUsername();
	}
	
	public static void main(String[] args) {
		EmergencyDaoImpl emergencyDaoImpl = new EmergencyDaoImpl();
		List<String> phoneList = emergencyDaoImpl.getPhones(3);
		for (String a : phoneList) {
			System.out.println(a);
		}
	}

}
