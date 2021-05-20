package com.pets.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pets.DAO.AdoptionRequestRepo;
import com.pets.DAO.LoginRepo;
import com.pets.DAO.PetRepo;
import com.pets.DTO.CreateRequestDTO;
import com.pets.DTO.UpdateAdoptionRequestDTO;
import com.pets.exception.BadInputException;
import com.pets.exception.CreationException;
import com.pets.exception.NotAuthorizedException;
import com.pets.exception.NotFoundException;
import com.pets.exception.UpdateException;
import com.pets.model.AdoptionRequest;
import com.pets.model.Pet;
import com.pets.model.User;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Service
public class AdoptionService {

	@Autowired
	AdoptionRequestRepo adoptionrepo;
	@Autowired
	PetRepo petrepo;
	@Autowired
	LoginRepo loginrepo;

	//Constructor for mocking
	public AdoptionService(AdoptionRequestRepo adoptionrepo) {
		super();
		this.adoptionrepo = adoptionrepo;
	}
	
	@Transactional(rollbackFor = {BadInputException.class, NotFoundException.class, CreationException.class})
	public AdoptionRequest createAdoptionRequest(CreateRequestDTO adoptionRequest, User loggedInUser) throws NotFoundException, BadInputException, CreationException {
		Pet pet;
		User user = loggedInUser;
		if(adoptionRequest.getPetId() > 0) {
			pet = petrepo.getPetById(adoptionRequest.getPetId());
		}else {
			throw new BadInputException("Pet id must be valid.");
		}
		if(adoptionRequest.getDescription() == null || adoptionRequest.getDescription().trim().equals("")) {
			throw new BadInputException("Request description cannot be blank.");
		}
		return adoptionrepo.createAdoptionRequest(user, pet, adoptionRequest.getDescription());
	}
	
	@Transactional(rollbackFor = NotFoundException.class)
	public List<AdoptionRequest> getAllAdoptionRequests(User loggedInUser) throws NotFoundException {
		if(loggedInUser.getUser_role().getUser_role().equalsIgnoreCase("customer")) {
			return adoptionrepo.getUserAllAdoptionRequest(loggedInUser);
		}else {
			return adoptionrepo.getManagerAllAdoptionRequest();
		}
	}
	
	@Transactional(rollbackFor = NotFoundException.class)
	public AdoptionRequest getRequestById(int id, User loggedInUser) throws NotFoundException {
		if(loggedInUser.getUser_role().getUser_role().equalsIgnoreCase("customer")) {
			return adoptionrepo.getUserAdoptionRequestById(id, loggedInUser);
		} else {
			return adoptionrepo.getManagerAdoptionRequestById(id);
		}
	}
	
	@Transactional(rollbackFor = {NotAuthorizedException.class, UpdateException.class, NotFoundException.class})
	public AdoptionRequest approveDenyRequest(int id, UpdateAdoptionRequestDTO update, User loggedInUser) throws NotAuthorizedException, UpdateException, NotFoundException {
		if(loggedInUser.getUser_role().getUser_role().equalsIgnoreCase("customer")) {
			throw new NotAuthorizedException("User is not authorized to approve/deny requests.");
		}else {
			return adoptionrepo.managerApproveDenyRequest(id, update);
		}
	}
}
