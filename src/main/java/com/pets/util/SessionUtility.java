package com.pets.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionUtility {
	private static Logger logger = LoggerFactory.getLogger(SessionUtility.class);
	private static String url;
	private static String user;
	private static String pass;
	
	private static SessionFactory sessionFactory;

	public synchronized static Session getSession() {
		if (sessionFactory == null) {
			getCreds();
			sessionFactory = new Configuration()
					.setProperty("hibernate.connection.url", url)
					.setProperty("hibernate.connection.username", user)
					.setProperty("hibernate.connection.password", pass)
					.configure("hibernate.cfg.xml").buildSessionFactory();
		}

		return sessionFactory.openSession();
	}
	
	private static void getCreds() {
				
	}
}
