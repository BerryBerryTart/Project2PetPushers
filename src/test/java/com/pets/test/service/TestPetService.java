package com.pets.test.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
		
		when(petRepo.createPet(testPet)).thenReturn(expectedPet);
		
		Pet actual = petService.createPet(testPet);
		
		assertEquals(expectedPet, actual);
	}
	
	@ParameterizedTest
	@MethodSource("invalidPetDTO")
	void testCreatePet_negative(int age, String name, String breed, String species, String description, String type) {
		Assertions.assertThrows(BadInputException.class, () -> {
			PetDTO testPet = new PetDTO();
			testPet.setPet_age(age);
			testPet.setPet_name(name);
			testPet.setPet_breed(breed);
			testPet.setPet_species(species);
			testPet.setPet_description(description);
			testPet.setPet_type(type);
			
			petService.createPet(testPet);
		  });
	}
	
	private static Stream<Arguments> invalidPetDTO(){
		return Stream.of(
				Arguments.of(0,"","","","",""),
				Arguments.of(1," ","  ","   ","    ","     "),
				Arguments.of(-1,"name","breed","species","description","real"),
				Arguments.of(1,"name","breed1","species","description","real"),
				Arguments.of(1,"name","breed","species1","description","real"),
				Arguments.of(1,"name","breed","species","description","not real"),
				Arguments.of(1,null,null,null,null,null)
				);
	}
	
	@Test
	void testGetAll_positive() throws NotFoundException {
		List<Pet> expected = new ArrayList<>();
		when(petRepo.getAllPets()).thenReturn(expected);
		
		List<Pet> actual = petService.getAllPets();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetById_positive() throws NotFoundException, BadInputException {
		Pet expectedPet = new Pet();
		when(petRepo.getPetById(1)).thenReturn(expectedPet);
		
		Pet actual = petService.getById(1);
		
		assertEquals(expectedPet, actual);
	}
	
	@ParameterizedTest
	@ValueSource(ints = {-1, 0})
	void testGetById_negative(int id) {
		Assertions.assertThrows(BadInputException.class, () -> {
			petService.getById(id);
		});
	}
	
	@Test
	void testUpdatePet_positive() throws UpdateException, BadInputException {
		Pet expectedPet = new Pet();
		PetDTO testPet = new PetDTO();
		testPet.setPet_age(1);
		testPet.setPet_name("name");
		testPet.setPet_breed("breed");
		testPet.setPet_species("species");
		testPet.setPet_description("description");
		testPet.setPet_type("real");
		when(petRepo.updatePetById(1, testPet)).thenReturn(expectedPet);
		
		Pet actual = petService.updatePet(1, testPet);
		
		assertEquals(expectedPet, actual);
	}
	
	@ParameterizedTest
	@ValueSource(ints = {-1, 0})
	void testUpdatePet_negative_id(int id) {
		Assertions.assertThrows(BadInputException.class, () -> {
			PetDTO validPet = new PetDTO();
			validPet.setPet_age(1);
			validPet.setPet_name("name");
			validPet.setPet_breed("breed");
			validPet.setPet_species("species");
			validPet.setPet_description("description");
			validPet.setPet_type("digital");
			petService.updatePet(id, validPet);
		});
	}
	
	@ParameterizedTest
	@MethodSource("invalidPetDTO")
	void testUpdatePet_negative_petDTO(int age, String name, String breed, String species, String description, String type) {
		Assertions.assertThrows(BadInputException.class, () -> {
			PetDTO testPet = new PetDTO();
			testPet.setPet_age(age);
			testPet.setPet_name(name);
			testPet.setPet_breed(breed);
			testPet.setPet_species(species);
			testPet.setPet_description(description);
			testPet.setPet_type(type);
			
			petService.updatePet(1, testPet);
		});
	}
	
	@Test
	void testDeletePet_positive() throws BadInputException, UpdateException {
		when(petRepo.deletePetById(1)).thenReturn(true);
		
		boolean actual = petService.deletePet(1);
		
		assertTrue(actual);
	}
	
	@ParameterizedTest
	@ValueSource(ints = {-1, 0})
	void testDeletePet_negative(int id) {
		Assertions.assertThrows(BadInputException.class, () -> {
			petService.deletePet(id);
		});
	}
}
