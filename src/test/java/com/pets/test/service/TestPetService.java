package com.pets.test.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pets.DAO.PetRepo;
import com.pets.DTO.PetDTO;
import com.pets.exception.BadInputException;
import com.pets.exception.NotFoundException;
import com.pets.exception.UpdateException;
import com.pets.model.Pet;
import com.pets.service.PetService;

@ExtendWith(MockitoExtension.class)
class TestPetService {
	@Mock
	PetRepo petRepo;
	@InjectMocks
	PetService petService;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testCreatePet_positive() throws BadInputException {
		Pet expectedPet = new Pet();
		PetDTO testPet = new PetDTO();
		testPet.setPet_age(1);
		testPet.setPet_name("name");
		testPet.setPet_breed("breed");
		testPet.setPet_species("species");
		testPet.setPet_description("description");
		testPet.setPet_type("real");
		
		Mockito.when(petRepo.createPet(testPet)).thenReturn(expectedPet);
		
		Pet actual = petService.createPet(testPet);
		
		assertEquals(expectedPet, actual);
	}
	
	@Test
	void testGetAll_positive() throws NotFoundException {
		List<Pet> expected = new ArrayList<>();
		Mockito.when(petRepo.getAllPets()).thenReturn(expected);
		
		List<Pet> actual = petService.getAllPets();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetById_positive() throws NotFoundException, BadInputException {
		Pet expectedPet = new Pet();
		Mockito.when(petRepo.getPetById(1)).thenReturn(expectedPet);
		
		Pet actual = petService.getById(1);
		
		assertEquals(expectedPet, actual);
	}
	
	@Test
	void testUpdatePet_positive() throws UpdateException, BadInputException {
		Pet expectedPet = new Pet();
		PetDTO testPet = new PetDTO();
		Mockito.when(petRepo.updatePetById(1, testPet)).thenReturn(expectedPet);
		
		Pet actual = petService.updatePet(1, testPet);
		
		assertEquals(expectedPet, actual);
	}
	
	@Test
	void testDeletePet_positive() throws BadInputException, UpdateException {
		Mockito.when(petRepo.deletePetById(1)).thenReturn(true);
		
		boolean actual = petService.deletePet(1);
		
		assertTrue(actual);
	}
}
