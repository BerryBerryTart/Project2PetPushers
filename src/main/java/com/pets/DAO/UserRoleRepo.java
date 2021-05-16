package com.pets.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pets.model.UserRole;

@Repository
public class UserRoleRepo {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public UserRole createUserRole(String roleName) {
		Session session = sessionFactory.getCurrentSession();
		
		UserRole newUserRole = new UserRole();
		
		newUserRole.setUser_role(roleName);
		
		session.persist(newUserRole);
		
		return newUserRole;
	}
}
