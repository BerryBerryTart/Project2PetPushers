package com.pets.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pets.DTO.CreateRequestDTO;
import com.pets.DTO.UpdateAdoptionRequestDTO;
import com.pets.exception.BadInputException;
import com.pets.exception.CreationException;
import com.pets.exception.NotAuthorizedException;
import com.pets.exception.NotFoundException;
import com.pets.exception.UpdateException;
import com.pets.model.AdoptionRequest;
import com.pets.model.User;
import com.pets.service.AdoptionService;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AdoptionController {
	
	@Autowired
	private AdoptionService adoptionService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	//Constructor for mock tests
	public AdoptionController(AdoptionService adoptionService) {
		super();
		this.adoptionService = adoptionService;
	}
	
	@PostMapping(path = "make_adoption_request")
	@ResponseStatus(HttpStatus.CREATED)
	public AdoptionRequest createAdoptionrequest(@RequestBody CreateRequestDTO adoptionRequest) {
		HttpSession session = request.getSession(false);
		User user = (User)session.getAttribute("loggedInUser");
		try {
			return adoptionService.createAdoptionRequest(adoptionRequest, user);
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (BadInputException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (CreationException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	@GetMapping(path = "view_adoption_status")
	@ResponseStatus(HttpStatus.OK)
	public List<AdoptionRequest> getAllRequests(){
		HttpSession session = request.getSession(false);
		User user = (User)session.getAttribute("loggedInUser");
		try {
			return adoptionService.getAllAdoptionRequests(user);
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@GetMapping(path = "view_adoption_status/{id}")
	@ResponseStatus(HttpStatus.OK)
	public AdoptionRequest getById(@PathVariable("id") int id) {
		HttpSession session = request.getSession(false);
		User user = (User)session.getAttribute("loggedInUser");
		try {
			return adoptionService.getRequestById(id, user);
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@PutMapping(path = "update_adoption_request/{id}")
	@ResponseStatus(HttpStatus.OK)
	public AdoptionRequest updateRequest(@PathVariable("id") int id, @RequestBody UpdateAdoptionRequestDTO adoptionRequest) {
		HttpSession session = request.getSession(false);
		User user = (User)session.getAttribute("loggedInUser");
		try {
			return adoptionService.approveDenyRequest(id, adoptionRequest, user);
		} catch (NotAuthorizedException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		} catch (UpdateException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
}
