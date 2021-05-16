package com.pets.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pets.exception.NotFoundException;
import com.pets.model.AdoptionRequest;
import com.pets.model.AdoptionRequestStatus;
import com.pets.model.Pet;
import com.pets.model.User;

@Repository
public class AdoptionRequestRepo {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public AdoptionRequest createAdoptionRequest(User user, Pet pet, String requestString) {
		Session session = sessionFactory.getCurrentSession();
		AdoptionRequest adoptRequest = new AdoptionRequest();
		
		//TODO: setup logic to check if a request already exists for a pet
		
		adoptRequest.setAdoption_request_user(user);
		adoptRequest.setAdoption_request_pet(pet);
		adoptRequest.setAdoption_request_description(requestString);
		
		//set request status
		AdoptionRequestStatus status = session.load(AdoptionRequestStatus.class, 1);
		adoptRequest.setAdoption_request_status(status);
		
		session.persist(adoptRequest);
		
		return adoptRequest;
	}
	
	@Transactional
	public List<AdoptionRequest> getAllAdoptionRequest(User user) throws NotFoundException{
		Session session = sessionFactory.getCurrentSession();
		List<AdoptionRequest> requestList = new ArrayList<>();
		
		String hql = "FROM AdoptionRequest ar WHERE ar.adoption_request_user=:user";
		
		try {
			Query query = session.createQuery(hql);
			query.setParameter("user", user);
			requestList = query.getResultList();
			
		} catch (NoResultException e) {
			throw new NotFoundException("No Adoption Request Not Found");
		}
		
		return requestList;
	}
}
