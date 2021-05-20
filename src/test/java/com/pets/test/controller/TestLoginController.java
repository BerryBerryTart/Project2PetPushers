package com.pets.test.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pets.DTO.CreateUserDTO;
import com.pets.DTO.LoginDTO;
import com.pets.controller.LoginController;
import com.pets.exception.BadInputException;
import com.pets.exception.CreationException;
import com.pets.exception.DatabaseExeption;
import com.pets.exception.NotFoundException;
import com.pets.model.User;
import com.pets.model.UserRole;
import com.pets.service.UserService;

@ExtendWith(MockitoExtension.class)
public class TestLoginController {
	@Autowired
	MockMvc mockmvc;
	@Mock
	UserService userService;
	@InjectMocks
	LoginController loginController;

	ObjectMapper om = new ObjectMapper();
	
	@BeforeEach
	void setup () throws NotFoundException, DatabaseExeption, BadInputException {
		this.mockmvc = MockMvcBuilders.standaloneSetup(loginController).build();
	}
	
	@Test
	void testRegisterAccount_positive () throws Exception {
		User expectedUser = new User(1,"username", "password", "firstname", "lastname", "email@email.com", new UserRole(1, "customer"));
		String expectedJson = om.writeValueAsString(expectedUser);
		CreateUserDTO testUser = new CreateUserDTO("firstname", "lastname", "username", "password", "email@email.com");
		String testJson = om.writeValueAsString(testUser);
		when(userService.createUser(testUser)).thenReturn(expectedUser);
		
		this.mockmvc.perform(
				MockMvcRequestBuilders.post("/register_account").contentType(MediaType.APPLICATION_JSON).content(testJson))
				.andExpect(status().isCreated()).andExpect(content().json(expectedJson));
	}
	
	@Test
	void testRegisterAccount_negative() throws Exception {
		CreateUserDTO testUser = new CreateUserDTO("firstname", "lastname", "username", "password", "email@email.com");
		String testJson = om.writeValueAsString(testUser);
		when(userService.createUser(testUser)).thenThrow(BadInputException.class);
		
		this.mockmvc.perform(
				MockMvcRequestBuilders.post("/register_account").contentType(MediaType.APPLICATION_JSON).content(testJson))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	void testLogin_positive() throws Exception {
		User expectedUser = new User(1,"username", "password", "firstname", "lastname", "email@email.com", new UserRole(1, "customer"));
		String expectedJson = om.writeValueAsString(expectedUser);
		LoginDTO testLogin = new LoginDTO("username","password");
		String testJson = om.writeValueAsString(testLogin);
		when(userService.login(testLogin)).thenReturn(expectedUser);
		
		this.mockmvc.perform(
				MockMvcRequestBuilders.post("/login_account").contentType(MediaType.APPLICATION_JSON).content(testJson))
				.andExpect(status().is2xxSuccessful()).andExpect(content().json(expectedJson));
	}
}
