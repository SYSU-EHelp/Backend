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

	public String getName(int id) {
		Session session = DBSessionUtil.getSession();
		User u = (User) session.get(User.class, id);
		String name = "";
		if (u != null) name = u.getUsername();
		DBSessionUtil.closeSession(session);
		return name;
	}
	
	//根据id获取用户
	public User getUser(int id) {
		Session session = DBSessionUtil.getSession();
		User u = (User) session.get(User.class, id);
		DBSessionUtil.closeSession(session);
		return u;
	}
	
	//添加验证码
	public boolean addCode(String phone, String code) {
		Session session = DBSessionUtil.getSession();
		Query query = session.createQuery(" from User u where u.phone=:phone");
		query.setParameter("phone", phone);
		User u = (User) query.uniqueResult();
		if (u != null) {
			u.setCode(code);
			session.update(u);
		}
		else {
			u = new User("", "", phone, "", code);
			session.save(u);
		}
		DBSessionUtil.closeSession(session);
		return true;
	}
	
	// 用以发送验证码时检查该手机号是否已被使用
	public boolean phoneExisted(String phone, String code) {
		Session session = DBSessionUtil.getSession();
		// 查询语句
		Query query = session.createQuery(" from User u where u.phone=:phone");
		// 设定查询语句中变量的值
		query.setParameter("phone", phone);
		// 查询结果
		User u = (User) query.uniqueResult();
		// 事务提交并关闭
		if (u != null && !u.getUsername().equals("")) {
			DBSessionUtil.closeSession(session);
			return true;
		}
		else return false;
	}
	
	// 注册添加用户
	public int addUser(String username, String password, String phone, String avatar, String code) {
		Session session = DBSessionUtil.getSession();
		Query query = session.createQuery(" from User u where u.username=:username");
		query.setParameter("username", username);
		User u = (User) query.uniqueResult();
		if (u != null) {
			DBSessionUtil.closeSession(session);
			return 0; //用户名已存在
		}
		
		query = session.createQuery(" from User u where u.phone=:phone");
		query.setParameter("phone", phone);
		u = (User) query.uniqueResult();
		if (u != null && code.equals(u.getCode())) {
			System.out.println(u.toString());
			u.setUsername(username);
			u.setPassword(password);
			u.setAvatar(avatar);
			session.update(u);
			DBSessionUtil.closeSession(session);
			return 1; //成功
		}
		else {
			DBSessionUtil.closeSession(session);
			return 2; //验证码错误
		}
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

	//紧急联系人数量
	public long getContactNum(int user_id) {
		Session session = DBSessionUtil.getSession();
		Query query = session.createQuery("select COUNT(*) from Contact c where c.user_id=:user_id");
		query.setParameter("user_id", user_id);
		long num = (long) query.uniqueResult();
		DBSessionUtil.closeSession(session);
		return num;
	}
	
	//添加紧急联系人
	public int addContact(Contact contact) {
		long contactNum = getContactNum(contact.getUser_id());
		if (contactNum >= 5) return 1;
		Session session = DBSessionUtil.getSession();		
		Query query = session.createQuery(" from Contact c where c.user_id=:user_id and c.contact_user=:contact_user and c.contact_phone=:contact_phone");
		query.setParameter("user_id", contact.getUser_id());
		query.setParameter("contact_user", contact.getContact_user());
		query.setParameter("contact_phone", contact.getContact_phone());
		Contact c = (Contact) query.uniqueResult();
		if (c != null) return 2;
		session.save(contact);
		DBSessionUtil.closeSession(session);
		return 0;
	}
	
	//删除紧急联系人
	public boolean deleteContact(int user_id, String username) {
		Session session = DBSessionUtil.getSession();
        Query query = session.createQuery(" from Contact c where c.user_id=:user_id and c.contact_user=:contact_user");
        query.setParameter("user_id", user_id);
        query.setParameter("contact_user", username);
        Contact c = (Contact) query.uniqueResult();
        session.delete(c);
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
				o[5] = 0;
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
		query = session.createQuery("select h.launcher_id, h.id, h.title, h.address, h.finished "
				+ "from Help h, Response r "
				+ "where r.user_id=:id and r.event_id=h.id");
		query.setParameter("id", id);
		que1 = query.list();
		
		for (Object[] o : que1) {
			o[0] = 1;
			query = session.createQuery("select u.username "
					+ "from Help h, User u "
					+ "where h.id=:id and h.launcher_id=u.id");
			query.setParameter("id", o[1]);
			o[3] = query.uniqueResult();
		}
		
		for (Object[] o : que) {
			results.add(o);
		}
		for (Object[] o : que1) {
			results.add(o);
		}
		
		DBSessionUtil.closeSession(session);
		return results;
	}

	public static void main(String[] args) {
		UserDaoImpl dao = new UserDaoImpl();
		System.out.println(dao.deleteContact(6, "ldn"));
		
		
//		List<Object[]> results = dao.getLaunch(3);
//		for (Object[] c : results) {
//			System.out.println("事件类型：" + c[0] + ", 事件标题：" + c[1] + "时间：" + c[3]);
//		}
		
//		List<Object[]> results = dao.getResponse(3);
//		for (Object[] c : results) {
//			System.out.println("事件类型：" + c[0] + ", 事件标题：" + c[2]);
//		}
		
	}

}



