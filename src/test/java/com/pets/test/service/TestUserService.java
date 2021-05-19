package com.pets.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pets.DAO.LoginRepo;
import com.pets.DTO.CreateUserDTO;
import com.pets.DTO.LoginDTO;
import com.pets.exception.BadInputException;
import com.pets.exception.CreationException;
import com.pets.exception.DatabaseExeption;
import com.pets.exception.NotFoundException;
import com.pets.model.User;
import com.pets.model.UserRole;
import com.pets.service.UserService;

@ExtendWith(MockitoExtension.class)
class TestUserService {
	@Mock
	LoginRepo loginRepo;
	@InjectMocks
	UserService userService;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testCreateUser_positive() throws CreationException, DatabaseExeption, BadInputException {
		User expectedUser = new User(1,"username", "password", "firstname", "lastname", "email@email.com", new UserRole(1, "customer"));
		CreateUserDTO testUser = new CreateUserDTO("firstname", "lastname", "username", "password", "email@email.com");
		Mockito.when(loginRepo.createUser(testUser))
			.thenReturn(expectedUser);
		
		User actual = userService.createUser(testUser);
		
		assertEquals(expectedUser, actual);
	}
	
	@Test
	void testLogin_positive() throws NotFoundException, DatabaseExeption {
		User expectedUser = new User(1,"username", "password", "firstname", "lastname", "email@email.com", new UserRole(1, "customer"));
		LoginDTO testCredentials = new LoginDTO("username", "password");
		Mockito.when(loginRepo.loginUser(testCredentials))
			.thenReturn(expectedUser);
		
		User actual = loginRepo.loginUser(testCredentials);
		
		assertEquals(expectedUser, actual);
	}
}
