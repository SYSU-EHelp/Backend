package com.ehelp.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.ehelp.entity.User;

@Repository
public class UserDao {

	private SessionFactory sessionFactory;
	private Session session;

	private void init() {
		// 获得配置对象
		Configuration config = new Configuration().configure();
		// 获得服务注册对象
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties())
				.buildServiceRegistry();
		// 获得sessionFactory对象
		sessionFactory = config.buildSessionFactory(serviceRegistry);
		// 获得session对象
		session = sessionFactory.openSession();
		session.beginTransaction();
	}

	// 用以注册时检查该手机号是否已被注册
	public boolean checkUser(String phone) {
		init();
		// 查询语句
		Query query = session.createQuery(" from User u where u.phone=:phone");
		// 设定查询语句中变量的值
		query.setParameter("phone", phone);
		// 查询结果
		User u = (User) query.uniqueResult();
		// 事务提交并关闭
		session.getTransaction().commit();
		session.close();
		if (u == null) {
			return true;
		}
		return false;
	}

	// 用以登录时检查数据库中是否存在该用户
	public boolean checkUser2(User user) {
		init();
		// 查询语句
		Query query = session.createQuery(" from User u where u.username=:username and u.password=:password");
		// 设定查询语句中变量的值
		query.setParameter("username", user.getUsername());
		query.setParameter("password", user.getPassword());
		// 查询结果
		User u = (User) query.uniqueResult();
		// 事务提交并关闭
		session.getTransaction().commit();
		session.close();
		if (u == null) {
			return false;
		}
		return true;
	}

	// 添加用户
	public boolean addUser(String phone, String username, String password) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(new User(phone, username, password));
		session.getTransaction().commit();
		session.close();
		return true;
	}

	public static void main(String[] args) {
		UserDao dao = new UserDao();
		System.out.println(dao.checkUser("1881925"));
		dao.addUser("188195", "admin", "123456");
		System.out.println(dao.checkUser("1881925"));
	}

}
