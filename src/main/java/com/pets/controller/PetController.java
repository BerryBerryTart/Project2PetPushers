package com.pets.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public ResponseEntity<Object> addPet(@RequestBody PetDTO petDTO){
		Pet pet;
		try {
			pet = petService.createPet(petDTO);
		} catch (BadInputException e) {
			return ResponseEntity.status(400).body(new MessageDTO(e.getMessage()));
		}
		return ResponseEntity.status(200).body(pet);
	}
	
	@GetMapping(path = "view_adoptable_pet")
	public ResponseEntity<Object> getAlllPets(){
		try {
			List<Pet> resultList = petService.getAllPets();
			return ResponseEntity.status(200).body(resultList);
		} catch (NotFoundException e) {
			return ResponseEntity.status(400).body(new MessageDTO(e.getMessage()));
		}
	}
	
	@GetMapping(path = "view_adoptable_pet/{id}")
	public ResponseEntity<Object> getById(@PathVariable("id") int id) {
		try {
			Pet pet = petService.getById(id);
			return ResponseEntity.status(200).body(pet);
		} catch (BadInputException e) {
			return ResponseEntity.status(400).body(new MessageDTO(e.getMessage()));
		} catch (NotFoundException e) {
			return ResponseEntity.status(404).body(new MessageDTO(e.getMessage()));
		}
	}
	
	@PostMapping(path = "view_pet_adoption/{id}")
	public ResponseEntity<Object> updatePet (@RequestBody PetDTO petDTO, @PathVariable int id){
		try {
			Pet pet = petService.updatePet(id, petDTO);
			return ResponseEntity.status(200).body(pet);
		} catch (UpdateException e) {
			return ResponseEntity.status(400).body(new MessageDTO(e.getMessage()));
		} catch (BadInputException e) {
			return ResponseEntity.status(400).body(new MessageDTO(e.getMessage()));
		}
	}
	
	@DeleteMapping(path = "view_adoptable_pet/{id}")
	public ResponseEntity<Object> deletePet (@PathVariable int id) {
		try {
			boolean deleteSuccessful = petService.deletePet(id);
			return ResponseEntity.status(201).body(deleteSuccessful);
		} catch (BadInputException e) {
			return ResponseEntity.status(400).body(new MessageDTO(e.getMessage()));
		} catch (UpdateException e) {
			return ResponseEntity.status(400).body(new MessageDTO(e.getMessage()));
		}
	}
}
