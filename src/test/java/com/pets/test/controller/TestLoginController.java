package com.pets.test.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pets.DTO.LoginDTO;
import com.pets.controller.LoginController;
import com.pets.exception.BadInputException;
import com.pets.exception.DatabaseExeption;
import com.pets.exception.NotFoundException;
import com.pets.model.User;
import com.pets.service.UserService;

@ExtendWith(MockitoExtension.class)
public class TestLoginController {
	MockMvc mockmvc;
	@Mock
	UserService userService;
	@InjectMocks
	LoginController loginController;
	
	@BeforeEach
	void setup () throws NotFoundException, DatabaseExeption, BadInputException {
		when(userService.login(new LoginDTO("username", "password"))).thenReturn(new User());
		this.mockmvc = MockMvcBuilders.standaloneSetup(loginController).build();
	}
}
