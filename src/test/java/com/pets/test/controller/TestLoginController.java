package com.pets.test.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
import com.pets.util.SessionUtility;

@ExtendWith(MockitoExtension.class)
public class TestLoginController {
	@Autowired
	MockMvc mockmvc;
	@Mock
	UserService userService;
	@InjectMocks
	LoginController loginController;

	private static Session mockSession;

	ObjectMapper om = new ObjectMapper();

	@BeforeEach
	void setup() throws NotFoundException, DatabaseExeption, BadInputException {
		mockSession = mock(Session.class);
		this.mockmvc = MockMvcBuilders.standaloneSetup(loginController).build();
	}

	@Test
	void testRegisterAccount_positive() throws Exception {
		User expectedUser = new User(1, "username", "password", "firstname", "lastname", "email@email.com",
				new UserRole(1, "customer"));
		String expectedJson = om.writeValueAsString(expectedUser);
		CreateUserDTO testUser = new CreateUserDTO("firstname", "lastname", "username", "password", "email@email.com");
		String testJson = om.writeValueAsString(testUser);
		when(userService.createUser(testUser)).thenReturn(expectedUser);

		this.mockmvc.perform(MockMvcRequestBuilders.post("/register_account").contentType(MediaType.APPLICATION_JSON)
				.content(testJson)).andExpect(status().isCreated()).andExpect(content().json(expectedJson));
	}

	@Test
	void testRegisterAccount_negative_badInput() throws Exception {
		CreateUserDTO testUser = new CreateUserDTO("firstname", "lastname", "username", "password", "email@email.com");
		String testJson = om.writeValueAsString(testUser);
		when(userService.createUser(testUser)).thenThrow(BadInputException.class);

		this.mockmvc.perform(MockMvcRequestBuilders.post("/register_account").contentType(MediaType.APPLICATION_JSON)
				.content(testJson)).andExpect(status().is4xxClientError());
	}
	
	@Test
	void testRegisterAccount_negative_creation() throws Exception {
		CreateUserDTO testUser = new CreateUserDTO("firstname", "lastname", "username", "password", "email@email.com");
		String testJson = om.writeValueAsString(testUser);
		when(userService.createUser(testUser)).thenThrow(CreationException.class);

		this.mockmvc.perform(MockMvcRequestBuilders.post("/register_account").contentType(MediaType.APPLICATION_JSON)
				.content(testJson)).andExpect(status().is5xxServerError());
	}

	@Test
	void testRegisterAccount_negative_database() throws Exception {
		CreateUserDTO testUser = new CreateUserDTO("firstname", "lastname", "username", "password", "email@email.com");
		String testJson = om.writeValueAsString(testUser);
		when(userService.createUser(testUser)).thenThrow(DatabaseExeption.class);

		this.mockmvc.perform(MockMvcRequestBuilders.post("/register_account").contentType(MediaType.APPLICATION_JSON)
				.content(testJson)).andExpect(status().is5xxServerError());
	}
	
	@Test
	void testLogin_positive() throws Exception {
		User expectedUser = new User(1, "username", "password", "firstname", "lastname", "email@email.com",
				new UserRole(1, "customer"));
		String expectedJson = om.writeValueAsString(expectedUser);
		LoginDTO testLogin = new LoginDTO("username", "password");
		String testJson = om.writeValueAsString(testLogin);
		when(userService.login(testLogin)).thenReturn(expectedUser);

		this.mockmvc
				.perform(MockMvcRequestBuilders.post("/login_account").contentType(MediaType.APPLICATION_JSON).content(testJson))
				.andExpect(status().is2xxSuccessful()).andExpect(content().json(expectedJson));
	}
	
	@Test
	void testLogin_negative_badInput() throws Exception {
		LoginDTO testLogin = new LoginDTO("username", "password");
		String testJson = om.writeValueAsString(testLogin);
		when(userService.login(testLogin)).thenThrow(BadInputException.class);
		
		this.mockmvc
		.perform(MockMvcRequestBuilders.post("/login_account").contentType(MediaType.APPLICATION_JSON).content(testJson))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	void testLogin_negative_notFound() throws Exception {
		LoginDTO testLogin = new LoginDTO("username", "password");
		String testJson = om.writeValueAsString(testLogin);
		when(userService.login(testLogin)).thenThrow(NotFoundException.class);
		
		this.mockmvc
		.perform(MockMvcRequestBuilders.post("/login_account").contentType(MediaType.APPLICATION_JSON).content(testJson))
		.andExpect(status().is4xxClientError());
	}
}
