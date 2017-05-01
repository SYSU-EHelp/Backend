package com.ehelp.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class DBSessionUtil {
	
	private static SessionFactory sessionFactory;
	private static Session session;

	public static Session getSession() {
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
		return session;
	}

	public static void closeSession() {
		session.getTransaction().commit();
		session.close();
	}
}
