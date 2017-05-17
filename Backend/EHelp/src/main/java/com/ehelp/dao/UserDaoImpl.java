package com.ehelp.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ehelp.entity.Contact;
import com.ehelp.entity.User;
import com.ehelp.util.DBSessionUtil;

@Repository
public class UserDaoImpl implements UserDao {

	// 用以注册时检查该手机号是否已被注册
	public boolean phoneExisted(String phone) {
		Session session = DBSessionUtil.getSession();
		// 查询语句
		Query query = session.createQuery(" from User u where u.phone=:phone");
		// 设定查询语句中变量的值
		query.setParameter("phone", phone);
		// 查询结果
		User u = (User) query.uniqueResult();
		// 事务提交并关闭
		DBSessionUtil.closeSession(session);
		if (u == null) {
			return false;
		}
		return true;
	}
	
	// 注册添加用户
	public boolean addUser(String username, String password, String phone, String avatar) {
		Session session = DBSessionUtil.getSession();
		session.save(new User(username, password, phone, avatar));
		DBSessionUtil.closeSession(session);
		return true;
	}

	// 用以登录时检查数据库中是否存在该用户
	public int checkUser(User user) {
		Session session = DBSessionUtil.getSession();
		// 查询语句
		Query query = session.createQuery(" from User u where u.username=:username and u.password=:password");
		// 设定查询语句中变量的值
		query.setParameter("username", user.getUsername());
		query.setParameter("password", user.getPassword());
		// 查询结果
		User u = (User) query.uniqueResult();
		// 事务提交并关闭
		DBSessionUtil.closeSession(session);
		if (u == null) {
			return -1;
		}
		return u.getId();
	}

	//添加紧急联系人
	public boolean addContact(Contact contact) {
		Session session = DBSessionUtil.getSession();
		session.save(contact);
		DBSessionUtil.closeSession(session);
		return true;
	}

	//获取紧急联系人
	public List<Contact> getContacts(int id) {
		List<Contact> contacts = new ArrayList<Contact>();
		Session session = DBSessionUtil.getSession();
		// 查询语句
		Query query = session.createQuery(" from Contact c where c.user_id=:user_id");
		// 设定查询语句中变量的值
		query.setParameter("user_id", id);
		query.setMaxResults(5);
		// 查询结果
		contacts = query.list();
		// 事务提交并关闭
		DBSessionUtil.closeSession(session);
		return contacts;
	}
	
	//获取我发起的事件
	public List<Object[]> getLaunch(int id) {
		List<Object[]> results = new ArrayList<Object[]>();
		
		//提问
		List<Object[]> que = new ArrayList<Object[]>();
		Session session = DBSessionUtil.getSession();
		Query query = session.createQuery("select q.asker_id, q.id, q.title, q.date, q.description "
				+ "from Question q, User u where q.asker_id=u.id and u.id=:id");
		query.setParameter("id", id);
		que = query.list();
		for (Object[] o : que) {
			int question_id = (Integer) o[1];
			query = session.createQuery("select count(*) "
					+ "from Question q, Answer a where q.id=:id and a.question_id=:id");
			query.setParameter("id", question_id);
			//由于不能指定Object[]大小，故多查询两个字段
			o[4] = query.uniqueResult();
			o[0] = 0;
		}
		
		//求助
		List<Object[]> que1 = new ArrayList<Object[]>();
		query = session.createQuery("select h.launcher_id, h.id, h.title, h.date, h.finished, h.address "
				+ "from Help h, User u where h.launcher_id=u.id and u.id=:id");
		query.setParameter("id", id);
		que1 = query.list();
		for (Object[] o : que1) {
			if ((Integer)o[4] == 1) {
				o[0] = 1;
				o[5] = null;
			}
			else {
				int help_id = (Integer) o[1];
				query = session.createQuery("select count(*) "
						+ "from Help h, Response r where h.id=:id and r.event_type=1 and r.event_id=:id");
				query.setParameter("id", help_id);
				o[5] = query.uniqueResult();
				o[0] = 1;
			}
		}
		
		//求救
		List<Object[]> que2 = new ArrayList<Object[]>();
		query = session.createQuery("select e.finished, e.id, e.launcher_id, e.date "
				+ "from Emergency e, User u where e.launcher_id=u.id and u.id=:id");
		query.setParameter("id", id);
		que2 = query.list();
		for (Object[] o : que2) {
			o[0] = 2;
		}
		DBSessionUtil.closeSession(session);
		
		for (Object[] o : que) {
			results.add(o);
		}
		for (Object[] o : que1) {
			results.add(o);
		}
		for (Object[] o : que2) {
			results.add(o);
		}
		//排序
		Collections.sort(results, new Comparator<Object[]>() {

			public int compare(Object[] o1, Object[] o2) {
				Date d1 = (Date) o1[3];
				Date d2 = (Date) o2[3];
				if (d1.after(d2)) return -1;
				else return 1;
			}
			
		});
		return results;
	}

	//获取我响应的事件
	public List<Object[]> getResponse(int id) {
		List<Object[]> results = new ArrayList<Object[]>();
		
		//提问
		List<Object[]> que = new ArrayList<Object[]>();
		Session session = DBSessionUtil.getSession();
		Query query = session.createQuery("select q.description, q.id, q.title, u1.username, q.date "
				+ "from Question q, User u, User u1, Answer a "
				+ "where a.answerer_id=u.id and u.id=:id and a.question_id=q.id and u1.id=q.asker_id");
		query.setParameter("id", id);
		que = query.list();
		for (Object[] o : que) {
			int question_id = (Integer) o[1];
			query = session.createQuery("select count(*) "
					+ "from Question q, Answer a where q.id=:id and a.question_id=:id");
			query.setParameter("id", question_id);
			o[4] = query.uniqueResult();
			o[0] = 0;
		}
		
		//求助
		List<Object[]> que1 = new ArrayList<Object[]>();
		query = session.createQuery("select h.launcher_id, h.id, h.title, u1.username, h.finished "
				+ "from Help h, User u, User u1, Response r "
				+ "where r.event_type=1 and r.user_id=:id and r.event_id=h.id and h.launcher_id=u1.id");
		query.setParameter("id", id);
		que1 = query.list();
		DBSessionUtil.closeSession(session);
		
		for (Object[] o : que1) {
			o[0] = 1;
		}
		
		for (Object[] o : que) {
			results.add(o);
		}
		for (Object[] o : que1) {
			results.add(o);
		}
		
		return results;
	}

	public static void main(String[] args) {
		UserDaoImpl dao = new UserDaoImpl();
		
		List<Object[]> results = dao.getResponse(3);
		for (Object[] c : results) {
			System.out.println(c[0]);
		}
		
	}

}



