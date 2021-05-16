package com.pets.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pets.model.AdoptionRequestStatus;
import com.pets.model.PetStatus;
import com.pets.model.PetType;
import com.pets.model.UserRole;

@Repository
public class EnumeratedRepo {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public UserRole createUserRole(String name) {
		Session session = sessionFactory.getCurrentSession();
		UserRole userRole = new UserRole();	
		userRole.setUser_role(name);
		session.persist(userRole);
		return userRole;
	}
	
	@Transactional
	public PetType createPetType(String name) {
		Session session = sessionFactory.getCurrentSession();
		PetType petType = new PetType();
		petType.setPet_type(name);
		session.persist(petType);
		return petType;
	}
	
	@Transactional
	public PetStatus createPetStatus(String name) {
		Session session = sessionFactory.getCurrentSession();
		PetStatus petStatus = new PetStatus();
		petStatus.setPet_status(name);
		session.persist(petStatus);
		return petStatus;
	}
	
	@Transactional
	public AdoptionRequestStatus createAdoptionStatus(String name) {
		Session session = sessionFactory.getCurrentSession();
		AdoptionRequestStatus adoptStatus = new AdoptionRequestStatus();
		adoptStatus.setAdoption_request_status(name);
		session.persist(adoptStatus);
		return adoptStatus;
	}
}
