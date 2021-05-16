package com.pets.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pets.DTO.UpdateAdoptionRequestDTO;
import com.pets.exception.CreationException;
import com.pets.exception.NotFoundException;
import com.pets.exception.UpdateException;
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

		// logic to check if a request already exists for a user
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
		} catch (NoResultException e) {
		}

		adoptRequest.setAdoption_request_user(user);
		adoptRequest.setAdoption_request_pet(pet);
		adoptRequest.setAdoption_request_description(requestString);

		String hql = "FROM AdoptionRequestStatus ars WHERE ars.adoption_request_status = :ars_status";
		Query<?> query = session.createQuery(hql);

		// set request status for real pets and automatically approve digital pets
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
	public List<AdoptionRequest> getUserAllAdoptionRequest(User user) throws NotFoundException {
		Session session = sessionFactory.getCurrentSession();
		List<AdoptionRequest> requestList = new ArrayList<>();

		String hql = "FROM AdoptionRequest ar WHERE ar.adoption_request_user=:user";
	
		Query<AdoptionRequest> query = session.createQuery(hql);
		query.setParameter("user", user);
		requestList = query.getResultList();

		return requestList;
	}

	@Transactional
	public AdoptionRequest getUserAdoptionRequestById(int id, User user) throws NotFoundException {
		Session session = sessionFactory.getCurrentSession();
		AdoptionRequest ar = null;

		String hql = "FROM AdoptionRequest ar WHERE ar.adoption_request_id=:id AND " + "ar.adoption_request_user=:user";
		try {
			Query<?> query = session.createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("user", user);
			ar = (AdoptionRequest) query.getSingleResult();
		} catch (NoResultException e) {
			throw new NotFoundException("Adoption Request with id " + id + " not found");
		}

		return ar;
	}

	// MANAGER PRIVS

	@Transactional
	public List<AdoptionRequest> getManagerAllAdoptionRequest() throws NotFoundException {
		Session session = sessionFactory.getCurrentSession();
		List<AdoptionRequest> requestList = new ArrayList<>();

		String hql = "FROM AdoptionRequest ar";
	
		Query<AdoptionRequest> query = session.createQuery(hql);
		requestList = query.getResultList();

		return requestList;
	}

	@Transactional
	public AdoptionRequest getManagerAdoptionRequestById(int id) throws NotFoundException {
		Session session = sessionFactory.getCurrentSession();
		AdoptionRequest ar = null;

		String hql = "FROM AdoptionRequest ar WHERE ar.adoption_request_id=:id";
		try {
			Query<?> query = session.createQuery(hql);
			query.setParameter("id", id);
			ar = (AdoptionRequest) query.getSingleResult();
		} catch (NoResultException e) {
			throw new NotFoundException("Adoption Request with id " + id + " not found");
		}

		return ar;
	}

	@Transactional
	public AdoptionRequest managerApproveDenyRequest(int id, UpdateAdoptionRequestDTO dto) throws UpdateException {
		Session session = sessionFactory.getCurrentSession();
		AdoptionRequest ar = null;

		// object setup
		String hqlStatus = "FROM AdoptionRequestStatus ars WHERE ars.adoption_request_status = :ars_status";
		Query<?> queryStatus = session.createQuery(hqlStatus);
		queryStatus.setParameter("ars_status", dto.getStatus());
		AdoptionRequestStatus ars = (AdoptionRequestStatus) queryStatus.getSingleResult();
		AdoptionRequestStatus status = session.load(AdoptionRequestStatus.class, ars.getAdoption_request_status_id());
		
		Date now = new Date();
		
		//actually update the request
		String hql = "UPDATE AdoptionRequest ar SET ar.adoption_request_response=:reason, "
				+ "ar.adoption_request_status=:status, ar.adoption_request_resolved=:now "
				+ "WHERE ar.adoption_request_id=:id";

		Query<?> query = session.createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("reason", dto.getReason());
		query.setParameter("status", status);
		query.setParameter("now", now);

		int count = query.executeUpdate();
		
		if (count <= 0) {
			throw new UpdateException("Failed to update request with id " + id);
		}
		
		//TODO: set all outstanding requests for other pets to N/A
		
		ar = session.get(AdoptionRequest.class, id);
		
		return ar;
	}

}
