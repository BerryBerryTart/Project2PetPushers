package com.pets.test.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pets.DTO.PetDTO;
import com.pets.controller.PetController;
import com.pets.exception.BadInputException;
import com.pets.model.Pet;
import com.pets.service.PetService;

@ExtendWith(MockitoExtension.class)
public class TestPetController {
	@Autowired
	MockMvc mockmvc;
	@Mock
	PetService petService;
	@InjectMocks
	PetController petController;

	ObjectMapper om = new ObjectMapper();

	@BeforeEach
	void setUp() throws Exception {
		this.mockmvc = MockMvcBuilders.standaloneSetup(petController).build();
	}

	@Test
	void testAddPet_positive() throws BadInputException {
		Pet expectedPet = new Pet();
		PetDTO testPet = new PetDTO();
		
		when(petService.createPet(testPet)).thenReturn(expectedPet);
		
		Pet actual = petController.addPet(testPet);
		
		assertEquals(expectedPet, ac);
	}

}
