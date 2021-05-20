package com.pets.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pets.DAO.PetRepo;
import com.pets.DTO.PetDTO;
import com.pets.exception.BadInputException;
import com.pets.exception.NotFoundException;
import com.pets.exception.UpdateException;
import com.pets.model.Pet;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Service
public class PetService {

	@Autowired
	private PetRepo petRepo;

	@Transactional(rollbackFor = BadInputException.class)
	public Pet createPet(PetDTO inputPet) throws BadInputException {
		if (inputPet.getPet_name() == null || inputPet.getPet_name().trim() == "") {
			throw new BadInputException("Pet name cannot be blank.");
		}
		if (inputPet.getPet_age() < 0) {
			throw new BadInputException("Pet age cannot be negative");
		}
		if (inputPet.getPet_species() == null || inputPet.getPet_species().trim() == "") {
			throw new BadInputException("Pet species cannot be blank.");
		}
		if (!inputPet.getPet_species().trim().matches("^[a-zA-Z]*$")) {
			throw new BadInputException("Pet species can only contain letters and spaces");
		}
		if (!inputPet.getPet_breed().trim().matches("^[a-zA-Z]*$")) {
			throw new BadInputException("Pet breed can only contain letters and spaces");
		}
		if (inputPet.getPet_description() == null || inputPet.getPet_description().trim() == "") {
			throw new BadInputException("Pet description cannot be blank.");
		}
		if (!(inputPet.getPet_type() == "real") && !(inputPet.getPet_type() == "digital")) {
			throw new BadInputException("Pet type can only be \"real\" or \"digital\"");
		}
		return petRepo.createPet(inputPet);
	}

	@Transactional(rollbackFor = NotFoundException.class)
	public List<Pet> getAllPets() throws NotFoundException {
		return petRepo.getAllPets();
	}

	@Transactional(rollbackFor = { BadInputException.class, NotFoundException.class })
	public Pet getById(int id) throws BadInputException, NotFoundException {
		if (id < 1) {
			throw new BadInputException("Invalid Id given: " + id);
		}
		return petRepo.getPetById(id);
	}

	@Transactional(rollbackFor = { UpdateException.class, BadInputException.class })
	public Pet updatePet(int id, PetDTO inputPet) throws UpdateException, BadInputException {
		if (id < 1) {
			throw new BadInputException("Invalid Id given: " + id);
		}
		if (inputPet.getPet_name() == null || inputPet.getPet_name().trim() == "") {
			throw new BadInputException("Pet name cannot be blank.");
		}
		if (inputPet.getPet_age() < 0) {
			throw new BadInputException("Pet age cannot be negative");
		}
		if (!inputPet.getPet_species().trim().matches("^[a-zA-Z]*$")) {
			throw new BadInputException("Pet species can only contain letters and spaces. Species cannot be blank.");
		}
		if (!inputPet.getPet_breed().trim().matches("^[a-zA-Z]*$")) {
			throw new BadInputException("Pet breed can only contain letters and spaces. Breed cannot be blank");
		}
		if (inputPet.getPet_description() == null || inputPet.getPet_description().trim() == "") {
			throw new BadInputException("Pet description cannot be blank.");
		}
		if (!(inputPet.getPet_type() == "real") && !(inputPet.getPet_type() == "digital")) {
			throw new BadInputException("Pet type can only be \"real\" or \"digital\"");
		}
		return petRepo.updatePetById(id, inputPet);
	}
	
	@Transactional(rollbackFor = {BadInputException.class, UpdateException.class})
	public boolean deletePet(int id) throws BadInputException, UpdateException {
		if (id < 1) {
			throw new BadInputException("Invalid Id given: " + id);
		}
		return petRepo.deletePetById(id);
	}
}
