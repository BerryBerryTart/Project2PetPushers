package com.pets.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pets.DAO.AdoptionRequestRepo;
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

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Service
public class AdoptionService {
	private static Logger logger = LoggerFactory.getLogger(AdoptionService.class);

	@Autowired
	AdoptionRequestRepo adoptionrepo;
	@Autowired
	PetRepo petrepo;
	
	@Transactional(rollbackFor = {BadInputException.class, NotFoundException.class, CreationException.class})
	public AdoptionRequest createAdoptionRequest(CreateRequestDTO adoptionRequest, User loggedInUser) throws NotFoundException, BadInputException, CreationException {
		Pet pet = new Pet();
		if(adoptionRequest.getPetId() > 0) {
			pet = petrepo.getPetById(adoptionRequest.getPetId());
		}else {
			logger.warn("AdoptionService.createAdoptionRequest() invalid id");
			throw new BadInputException("Pet id must be valid.");
		}
		if(adoptionRequest.getDescription() == null || adoptionRequest.getDescription().trim().equals("")) {
			logger.warn("AdoptionService.createAdoptionRequest() blank description");
			throw new BadInputException("Request description cannot be blank.");
		}
		return adoptionrepo.createAdoptionRequest(loggedInUser, pet, adoptionRequest.getDescription());
	}
	
	@Transactional(rollbackFor = NotFoundException.class)
	public List<AdoptionRequest> getAllAdoptionRequests(User loggedInUser) throws NotFoundException, NotAuthorizedException {
		if(loggedInUser.getUser_role().getUser_role().equalsIgnoreCase("customer")) {
			return adoptionrepo.getUserAllAdoptionRequest(loggedInUser);
		}else if(loggedInUser.getUser_role().getUser_role().equalsIgnoreCase("manager")){
			return adoptionrepo.getManagerAllAdoptionRequest();
		}else {
			logger.warn("AdoptionService.getAllAdoptionRequests() invalid user role");
			throw new NotAuthorizedException("User role must be customer or mangaer.");
		}
	}
	
	@Transactional(rollbackFor = NotFoundException.class)
	public AdoptionRequest getRequestById(int id, User loggedInUser) throws NotFoundException, NotAuthorizedException, BadInputException {
		if(id < 1) {
			logger.warn("AdoptionService.getRequestById() invalid id");
			throw new BadInputException("Request id must be valid");
		}
		if(loggedInUser.getUser_role().getUser_role().equalsIgnoreCase("customer")) {
			return adoptionrepo.getUserAdoptionRequestById(id, loggedInUser);
		} else if(loggedInUser.getUser_role().getUser_role().equalsIgnoreCase("manager")){
			return adoptionrepo.getManagerAdoptionRequestById(id);
		}else {
			logger.warn("AdoptionService.getRequestById() invalid user role");
			throw new NotAuthorizedException("User role must be customer or mangaer.");
		}
	}
	
	@Transactional(rollbackFor = {NotAuthorizedException.class, UpdateException.class, NotFoundException.class})
	public AdoptionRequest approveDenyRequest(int id, UpdateAdoptionRequestDTO update, User loggedInUser) throws NotAuthorizedException, UpdateException, NotFoundException {
		if(loggedInUser.getUser_role().getUser_role().equalsIgnoreCase("manager")) {
			return adoptionrepo.managerApproveDenyRequest(id, update);
		}else {
			logger.warn("AdoptionService.getRequestById() invalid user role");
			throw new NotAuthorizedException("User is not authorized to approve/deny requests.");
		}
	}
}
