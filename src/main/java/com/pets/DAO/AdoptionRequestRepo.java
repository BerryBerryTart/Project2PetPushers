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

import com.pets.exception.CreationException;
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
	public AdoptionRequest createAdoptionRequest(User user, Pet pet, String requestString) throws CreationException {
		Session session = sessionFactory.getCurrentSession();
		AdoptionRequest adoptRequest = new AdoptionRequest();
		
		//logic to check if a request already exists for a user
		String hqlRequestCheck = "FROM AdoptionRequest ars WHERE ars.adoption_request_pet=:pet AND "
				+ "ars.adoption_request_user=:user";
		try {
			Query<?> reqCheckQuery = session.createQuery(hqlRequestCheck);
			reqCheckQuery.setParameter("user", user);
			reqCheckQuery.setParameter("pet", pet);
			AdoptionRequest checkedRequest = (AdoptionRequest) reqCheckQuery.getSingleResult();
			if (checkedRequest != null) {
				throw new CreationException("Request For This Pet Already Exists");
			}
		} catch (NoResultException e) {}
		
		adoptRequest.setAdoption_request_user(user);
		adoptRequest.setAdoption_request_pet(pet);
		adoptRequest.setAdoption_request_description(requestString);
		
		String hql = "FROM AdoptionRequestStatus ars WHERE ars.adoption_request_status = :ars_status";
		Query<?> query = session.createQuery(hql);
		
		//set request status for real pets and automatically approve digital pets
		if (pet.getPet_type().getPet_type().equals("real")) {
			query.setParameter("ars_status", "pending");		
		} else if (pet.getPet_type().getPet_type().equals("digital")) {
			query.setParameter("ars_status", "approved");
		}
		
		AdoptionRequestStatus ars = (AdoptionRequestStatus) query.getSingleResult();
		AdoptionRequestStatus status = session.load(AdoptionRequestStatus.class, ars.getAdoption_request_status_id());
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
			Query<AdoptionRequest> query = session.createQuery(hql);
			query.setParameter("user", user);
			requestList = query.getResultList();
			
		} catch (NoResultException e) {
			throw new NotFoundException("No Adoption Request Not Found");
		}
		
		return requestList;
	}
}
