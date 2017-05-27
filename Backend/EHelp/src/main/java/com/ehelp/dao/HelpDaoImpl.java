package com.ehelp.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ehelp.entity.Help;
import com.ehelp.entity.Response;
import com.ehelp.util.DBSessionUtil;

@Repository
public class HelpDaoImpl implements HelpDao {

	//获取求助列表
	public List<Object[]> getAllHelps() {
		List<Object[]> results = new ArrayList<Object[]>();
		Session session = DBSessionUtil.getSession();
		// 查询语句
		Query query = session.createQuery("select h.id, h.title, h.description, h.address, h.finished, h.date, u.username, u.avatar, u.phone "
				+ "from Help h, User u where h.launcher_id=u.id");
		// 查询结果
		results = query.list();
		// 事务提交并关闭
		DBSessionUtil.closeSession(session);
		return results;
	}

	//根据id查看求助详情
	public List<Object[]> getHelp(int id) {
		List<Object[]> results = new ArrayList<Object[]>();
		Session session = DBSessionUtil.getSession();
		// 查询语句
		Query query = session.createQuery("select h.id, h.title, h.description, h.address, h.finished, h.date, "
				+ "u.username, u.avatar, u.phone, h.longitude, h.latitude "
				+ "from Help h, User u where h.launcher_id=u.id and h.id=:id");
		query.setParameter("id", id);
		// 查询结果
		results = query.list();
		// 事务提交并关闭
		DBSessionUtil.closeSession(session);
		return results;
	}

	//响应求助
	public boolean responseHelp(Response r) {
		Session session = DBSessionUtil.getSession();
		session.save(r);
		DBSessionUtil.closeSession(session);
		return true;
	}

	//结束求助
	public boolean endHelp(int id) {
		Session session = DBSessionUtil.getSession();
		Help h = (Help)session.get(Help.class, id);
		h.setFinished(1);
		session.save(h);
		DBSessionUtil.closeSession(session);
		return true;
	}

	//添加求助
	public boolean launchHelp(Help p) {
		Session session = DBSessionUtil.getSession();
		session.save(p);
		DBSessionUtil.closeSession(session);
		return true;
	}

	//根据id查看响应详情
	public List<Object[]> getAllResponse(int id) {
		List<Object[]> results = new ArrayList<Object[]>();
		Session session = DBSessionUtil.getSession();
		// 查询语句
		Query query = session.createQuery("select u.username, u.avatar, u.phone "
				+ "from Response r, User u where r.user_id=u.id and r.event_type=1 and r.event_id=:id");
		query.setParameter("id", id);
		// 查询结果
		results = query.list();
		// 事务提交并关闭
		DBSessionUtil.closeSession(session);
		return results;
	}

	public static void main(String[] args) {
		HelpDaoImpl h = new HelpDaoImpl();
		List<Object[]> results = h.getAllResponse(1);
		for (Object[] o : results) {
			System.out.println((String)o[0]);
		}
		
	}
	
	
}
