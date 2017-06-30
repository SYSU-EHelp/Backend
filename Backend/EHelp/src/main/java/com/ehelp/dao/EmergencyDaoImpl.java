package com.ehelp.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	
	//发起求救并返回id
	public int launchEmergency(Emergency e) {
		Session session = DBSessionUtil.getSession();
		session.save(e);
		DBSessionUtil.closeSession(session);
		//返回id
		session = DBSessionUtil.getSession();
		Query query = session.createQuery("select e.id from Emergency e where e.launcher_id=:launcher_id and e.date=:date");
		query.setParameter("launcher_id", e.getLauncher_id());
		query.setParameter("date", e.getDate());
		if (query.uniqueResult() == null) return -1;
		int id = (int) query.uniqueResult();
		return id;
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
	
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		EmergencyDaoImpl emergencyDaoImpl = new EmergencyDaoImpl();
		Emergency e = new Emergency(23, sdf.parse(sdf.format(new Date())), 0);
		System.out.println(emergencyDaoImpl.launchEmergency(e));
		
	}

}
