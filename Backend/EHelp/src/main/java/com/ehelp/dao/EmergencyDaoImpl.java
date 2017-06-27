package com.ehelp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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
	
	//结束求救
	public boolean stopEmergency(int id) {
		Session session = DBSessionUtil.getSession();
		Query query = session.createQuery(" from Emergency e where e.id=:id");
		query.setParameter("id", id);
		Emergency e = (Emergency)query.uniqueResult();
		e.setFinished(1);
		session.update(e);
		DBSessionUtil.closeSession(session);
		return true;
	}

	//获取紧急联系人电话
	public List<String> getPhones(int id) {
		List<String> list = new ArrayList<String>();
		Session session = DBSessionUtil.getSession();
		Query query = session.createQuery("select c.contact_phone "
				+ "from User u, Contact c where u.id=:id and u.id=c.user_id");
		query.setParameter("id", id);
		list = query.list();
		DBSessionUtil.closeSession(session);
		return list;
	}
	
	public static void main(String[] args) {
		EmergencyDaoImpl emergencyDaoImpl = new EmergencyDaoImpl();
//		List<String> list = emergencyDaoImpl.getPhones(3);
//		for (String s : list) System.out.println(s);
		System.out.println(emergencyDaoImpl.stopEmergency(2));
		
	}

}
