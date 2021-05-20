package com.pets.test.controller;

import static org.junit.jupiter.api.Assertions.*;

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
import com.pets.controller.PetController;
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
	void test() {
		fail("Not yet implemented");
	}

}
