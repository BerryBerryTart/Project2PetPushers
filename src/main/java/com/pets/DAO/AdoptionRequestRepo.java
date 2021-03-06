package com.pets.DAO;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.pets.model.PetStatus;
import com.pets.model.User;

@Repository
public class AdoptionRequestRepo {
	private static Logger logger = LoggerFactory.getLogger(AdoptionRequestRepo.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	private static String arsColName = "ars_status";
	
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
			logger.debug("Creating adoption request");
		}

		adoptRequest.setAdoption_request_user(user);
		adoptRequest.setAdoption_request_pet(pet);
		adoptRequest.setAdoption_request_description(requestString);

		String hql = "FROM AdoptionRequestStatus ars WHERE ars.adoption_request_status = :ars_status";
		Query<?> query = session.createQuery(hql);

		// set request status for real pets and automatically approve digital pets
		if (pet.getPet_type().getPet_type().equals("real")) {
			query.setParameter(arsColName, "pending");
		} else if (pet.getPet_type().getPet_type().equals("digital")) {
			query.setParameter(arsColName, "approved");
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
		List<AdoptionRequest> requestList = null;

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
		List<AdoptionRequest> requestList = null;

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
	public AdoptionRequest managerApproveDenyRequest(int id, UpdateAdoptionRequestDTO dto) throws UpdateException, NotFoundException {
		Session session = sessionFactory.getCurrentSession();
		AdoptionRequest ar = null;
		
		//logic to check if a request has been approved / denied
		//if so then throw an error
		String hqlCheck = "FROM AdoptionRequest ar WHERE ar.adoption_request_id=:id";		
		try {
			Query<?> query = session.createQuery(hqlCheck);
			query.setParameter("id", id);
			ar = (AdoptionRequest) query.getSingleResult();
			String status = ar.getAdoption_request_status().getAdoption_request_status();
			if (status.equals("approved") || status.equals("rejected")) {
				throw new UpdateException("Request has already been " + status);
			}
		} catch (NoResultException e) {
			throw new NotFoundException("Adoption Request with id " + id + " not found");
		}
		
		// object setup
		String hqlStatus = "FROM AdoptionRequestStatus ars WHERE ars.adoption_request_status = :ars_status";
		Query<?> queryStatus = session.createQuery(hqlStatus);
		queryStatus.setParameter(arsColName, dto.getStatus());
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
		
		//fallback just in case
		if (count <= 0) {
			throw new UpdateException("Failed to update request with id " + id);
		}
		
		//set all outstanding requests for other pets to N/A
		// object setup
		hqlStatus = "FROM AdoptionRequestStatus ars WHERE ars.adoption_request_status = :ars_status";
		queryStatus = session.createQuery(hqlStatus);
		queryStatus.setParameter(arsColName, "rejected");
		ars = (AdoptionRequestStatus) queryStatus.getSingleResult();
		AdoptionRequestStatus newStatus = session.load(AdoptionRequestStatus.class, ars.getAdoption_request_status_id());
		
		//NOW set all outstanding requests for other pets to N/A
		String hqlUpdateOthers = "UPDATE AdoptionRequest ar SET ar.adoption_request_status=:status "
				+ "WHERE ar.adoption_request_id!=:id";
		queryStatus = session.createQuery(hqlUpdateOthers);
		queryStatus.setParameter("status", newStatus);
		queryStatus.setParameter("id", id);
		queryStatus.executeUpdate();
		
		//change pet's status to adopted
		hqlStatus = "FROM PetStatus ps WHERE ps.pet_status=:pet_status";		
		queryStatus = session.createQuery(hqlStatus);
		queryStatus.setParameter("pet_status", "adopted");	
		PetStatus ps = (PetStatus) queryStatus.getSingleResult();		
		PetStatus newPetStatus = session.load(PetStatus.class, ps.getPet_status_id());
		Pet pet = ar.getAdoption_request_pet();
		pet.setPet_status(newPetStatus);
		session.persist(pet);
		
		//set return object params
		ar.setAdoption_request_response(dto.getReason());
		ar.setAdoption_request_status(status);
		ar.setAdoption_request_resolved(now);
		
		return ar;
	}

}
