package com.ehelp.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
		Query query = session.createQuery("select h.id, h.title, h.description, h.address, h.finished, h.date, u.username, u.avatar, u.phone, "
				+ "h.longitude, h.latitude "
				+ "from Help h, User u where h.launcher_id=u.id and h.finished=0");
		// 查询结果
		results = query.list();
		//排序
		Collections.sort(results, new Comparator<Object[]>() {

			public int compare(Object[] o1, Object[] o2) {
				Date d1 = (Date) o1[5];
				Date d2 = (Date) o2[5];
				if (d1.after(d2)) return -1;
				else return 1;
			}
			
		});
		
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
	public int responseHelp(Response r) {
		Session session = DBSessionUtil.getSession();
		Help h = (Help) session.get(Help.class, r.getEvent_id());
		if (h.getFinished() == 1) {
			DBSessionUtil.closeSession(session);
			return 1;
		}
		//只能同时响应一个求助
		Query query = session.createQuery("select r.id from Response r, Help h "
				+ "where r.user_id=:user_id and r.event_id=h.id and h.finished=0");
		query.setParameter("user_id", r.getUser_id());
		List<Object> results = new ArrayList<Object>();
		results = query.list();
		if (results.size() > 0) {
			DBSessionUtil.closeSession(session);
			return 2;
		}
		session.save(r);
		DBSessionUtil.closeSession(session);
		return 0;
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
	public int launchHelp(Help p) {
		Session session = DBSessionUtil.getSession();
		session.save(p);
		DBSessionUtil.closeSession(session);
		return getHelpId(p);
	}
	
	public int getHelpId(Help p) {
		Session session = DBSessionUtil.getSession();
		Query query = session.createQuery(" from Help h where h.title=:title and h.description=:description and h.date=:date");
		query.setParameter("title", p.getTitle());
		query.setParameter("description", p.getDescription());
		query.setParameter("date", p.getDate());
		Help help = (Help) query.uniqueResult();
		if (help == null) return -1;
		System.out.println(help.toString());
		DBSessionUtil.closeSession(session);
		return help.getId();
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
		List<Object[]> results = h.getAllHelps();
		System.out.println(results.size());
		for (Object[] o : results) {
			System.out.println(o[9]);
		}
		
	}
	
	
}
