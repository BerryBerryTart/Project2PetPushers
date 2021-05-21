package com.pets.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import com.pets.DTO.MessageDTO;
import com.pets.DTO.PetDTO;
import com.pets.exception.BadInputException;
import com.pets.exception.NotFoundException;
import com.pets.exception.UpdateException;
import com.pets.model.Pet;
import com.pets.service.PetService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Controller
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class PetController {
	@Autowired
	private PetService petService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	@PostMapping(path = "create_pet_adoption")
	@ResponseStatus(code =  HttpStatus.CREATED)
	public Pet addPet(@RequestBody PetDTO petDTO){
		Pet pet;
		try {
			pet = petService.createPet(petDTO);
			return pet;
		} catch (BadInputException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@GetMapping(path = "view_adoptable_pet")
	@ResponseStatus(code =  HttpStatus.OK)
	public List<Pet> getAlllPets(){
		try {
			List<Pet> resultList = petService.getAllPets();
			return resultList;
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@GetMapping(path = "view_adoptable_pet/{id}")
	@ResponseStatus(code =  HttpStatus.OK)
	public Pet getById(@PathVariable("id") int id) {
		try {
			Pet pet = petService.getById(id);
			return pet;
		} catch (BadInputException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@PostMapping(path = "update_pet_adoption/{id}")
	@ResponseStatus(code =  HttpStatus.OK)
	public Pet updatePet (@RequestBody PetDTO petDTO, @PathVariable int id){
		try {
			Pet pet = petService.updatePet(id, petDTO);
			return pet;
		} catch (UpdateException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (BadInputException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@DeleteMapping(path = "delete_pet_adoption/{id}")
	@ResponseStatus(code =  HttpStatus.OK)
	public boolean deletePet (@PathVariable int id) {
		try {
			boolean deleteSuccessful = petService.deletePet(id);
			return deleteSuccessful;
		} catch (BadInputException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (UpdateException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
