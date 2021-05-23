package com.pets.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static Logger logger = LoggerFactory.getLogger(PetService.class);

	@Autowired
	private PetRepo petRepo;

	@Transactional(rollbackFor = BadInputException.class)
	public Pet createPet(PetDTO inputPet) throws BadInputException {
		if (inputPet.getPet_name() == null || inputPet.getPet_name().trim().equals("")) {
			logger.warn("PetService.createPet() blank name");
			throw new BadInputException("Pet name cannot be blank.");
		}
		if (inputPet.getPet_age() < 0) {
			logger.warn("PetService.createPet() negative age");
			throw new BadInputException("Pet age cannot be negative");
		}
		if (!inputPet.getPet_species().trim().matches("^[a-zA-Z ]*$")) {
			logger.warn("PetService.createPet() invalid species");
			throw new BadInputException("Pet species can only contain letters and spaces");
		}
		if (!inputPet.getPet_breed().trim().matches("^[a-zA-Z ]*$")) {
			logger.warn("PetService.createPet() invalid breed");
			throw new BadInputException("Pet breed can only contain letters and spaces");
		}
		if (inputPet.getPet_description() == null || inputPet.getPet_description().trim().equals("")) {
			logger.warn("PetService.createPet() blank description");
			throw new BadInputException("Pet description cannot be blank.");
		}
		if (!(inputPet.getPet_type().equals("real") ) && !(inputPet.getPet_type().equals("digital") )) {
			logger.warn("PetService.createPet() invalid type");
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
			logger.warn("PetService.getById() invalid id");
			throw new BadInputException("Invalid Id given: " + id);
		}
		return petRepo.getPetById(id);
	}

	@Transactional(rollbackFor = { UpdateException.class, BadInputException.class })
	public Pet updatePet(int id, PetDTO inputPet) throws UpdateException, BadInputException {
		if (id < 1) {
			logger.warn("PetService.updatePet() invalid id");
			throw new BadInputException("Invalid Id given: " + id);
		}
		if (inputPet.getPet_name() == null || inputPet.getPet_name().trim().equals("")) {
			logger.warn("PetService.updatePet() blank name");
			throw new BadInputException("Pet name cannot be blank.");
		}
		if (inputPet.getPet_age() < 0) {
			logger.warn("PetService.updatePet() negative age");
			throw new BadInputException("Pet age cannot be negative");
		}
		if (!inputPet.getPet_species().trim().matches("^[a-zA-Z ]*$")) {
			logger.warn("PetService.updatePet() invalid species");
			throw new BadInputException("Pet species can only contain letters and spaces. Species cannot be blank.");
		}
		if (!inputPet.getPet_breed().trim().matches("^[a-zA-Z ]*$")) {
			logger.warn("PetService.updatePet() invalid breed");
			throw new BadInputException("Pet breed can only contain letters and spaces. Breed cannot be blank");
		}
		if (inputPet.getPet_description() == null || inputPet.getPet_description().trim().equals("")) {
			logger.warn("PetService.updatePet() blank description");
			throw new BadInputException("Pet description cannot be blank.");
		}
		if (!(inputPet.getPet_type().equals("real") ) && !(inputPet.getPet_type().equals("digital") )) {
			logger.warn("PetService.updatePet() invalid type");
			throw new BadInputException("Pet type can only be \"real\" or \"digital\"");
		}
		return petRepo.updatePetById(id, inputPet);
	}
	
	@Transactional(rollbackFor = {BadInputException.class, UpdateException.class})
	public boolean deletePet(int id) throws BadInputException, UpdateException {
		if (id < 1) {
			logger.warn("PetService.deletePet() invalid id");
			throw new BadInputException("Invalid Id given: " + id);
		}
		return petRepo.deletePetById(id);
	}
}
