package com.pets.test.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pets.DTO.PetDTO;
import com.pets.controller.PetController;
import com.pets.exception.BadInputException;
import com.pets.exception.NotFoundException;
import com.pets.exception.UpdateException;
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

	private ObjectMapper om = new ObjectMapper();

	@BeforeEach
	void setUp() throws Exception {
		this.mockmvc = MockMvcBuilders.standaloneSetup(petController).build();
	}

	@Test
	void testAddPet_positive() throws Exception {
		Pet expectedPet = new Pet();
		String expectedJson = om.writeValueAsString(expectedPet);
		PetDTO testPet = new PetDTO();
		String testJson = om.writeValueAsString(testPet);

		when(petService.createPet(testPet)).thenReturn(expectedPet);

		this.mockmvc.perform(post("/create_pet_adoption").contentType(MediaType.APPLICATION_JSON).content(testJson))
				.andExpect(status().isCreated()).andExpect(content().json(expectedJson));
	}

	@Test
	void testAddPet_negative() throws Exception {
		PetDTO testPet = new PetDTO();
		String testJson = om.writeValueAsString(testPet);

		when(petService.createPet(testPet)).thenThrow(BadInputException.class);

		this.mockmvc.perform(post("/create_pet_adoption").contentType(MediaType.APPLICATION_JSON).content(testJson))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testGetAllPets_positive() throws Exception {
		List<Pet> expected = new ArrayList<>();
		String expectedJson = om.writeValueAsString(expected);

		when(petService.getAllPets()).thenReturn(expected);

		this.mockmvc.perform(get("/view_adoptable_pet")).andExpect(status().isOk())
				.andExpect(content().json(expectedJson));
	}

	@Test
	void testGetAllPets_negative() throws Exception {
		when(petService.getAllPets()).thenThrow(NotFoundException.class);

		this.mockmvc.perform(get("/view_adoptable_pet")).andExpect(status().isNotFound());
	}

	@Test
	void testGetPetById_positive() throws Exception {
		Pet expected = new Pet();
		String expectedJson = om.writeValueAsString(expected);

		when(petService.getById(eq(1))).thenReturn(expected);

		this.mockmvc.perform(get("/view_adoptable_pet/1")).andExpect(status().isOk())
				.andExpect(content().json(expectedJson));
	}

	@Test
	void testGetPetById_negative_badInput() throws Exception {
		when(petService.getById(eq(1))).thenThrow(BadInputException.class);

		this.mockmvc.perform(get("/view_adoptable_pet/1")).andExpect(status().isBadRequest());
	}

	@Test
	void testGetPetById_negative_notFound() throws Exception {
		when(petService.getById(eq(1))).thenThrow(NotFoundException.class);

		this.mockmvc.perform(get("/view_adoptable_pet/1")).andExpect(status().isNotFound());
	}

	@Test
	void testUpdatePet_positive() throws Exception {
		Pet expected = new Pet();
		String expectedJson = om.writeValueAsString(expected);
		PetDTO testPet = new PetDTO();
		String testJson = om.writeValueAsString(testPet);

		when(petService.updatePet(eq(1), eq(testPet))).thenReturn(expected);

		this.mockmvc.perform(put("/update_pet_adoption/1").contentType(MediaType.APPLICATION_JSON).content(testJson))
				.andExpect(status().isOk()).andExpect(content().json(expectedJson));
	}

	@Test
	void testUpdatePet_negative_update() throws Exception {
		PetDTO testPet = new PetDTO();
		String testJson = om.writeValueAsString(testPet);

		when(petService.updatePet(eq(1), eq(testPet))).thenThrow(UpdateException.class);

		this.mockmvc.perform(put("/update_pet_adoption/1").contentType(MediaType.APPLICATION_JSON).content(testJson))
				.andExpect(status().isInternalServerError());
	}

	@Test
	void testUpdatePet_negative_badInput() throws Exception {
		PetDTO testPet = new PetDTO();
		String testJson = om.writeValueAsString(testPet);

		when(petService.updatePet(eq(1), eq(testPet))).thenThrow(BadInputException.class);

		this.mockmvc.perform(put("/update_pet_adoption/1").contentType(MediaType.APPLICATION_JSON).content(testJson))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testDeletePet_postive() throws Exception {

		when(petService.deletePet(eq(1))).thenReturn(true);

		this.mockmvc.perform(delete("/delete_pet_adoption/1")).andExpect(status().isOk());
	}

	@Test
	void testDeletePet_negative_badInput() throws Exception {

		when(petService.deletePet(eq(1))).thenThrow(BadInputException.class);

		this.mockmvc.perform(delete("/delete_pet_adoption/1")).andExpect(status().isBadRequest());
	}
	
	@Test
	void testDeletePet_negative_update() throws Exception {

		when(petService.deletePet(eq(1))).thenThrow(UpdateException.class);

		this.mockmvc.perform(delete("/delete_pet_adoption/1")).andExpect(status().isInternalServerError());
	}
}
