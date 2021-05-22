package com.pets.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
		when(loginRepo.createUser(testUser))
			.thenReturn(expectedUser);
		
		User actual = userService.createUser(testUser);
		
		assertEquals(expectedUser, actual);
	}
	
	@ParameterizedTest
	@MethodSource("invalidUsers")
	void testCreateUser_negative(String firstname, String lastname, String username, String password, String email) {
		Assertions.assertThrows(BadInputException.class, () -> {
			CreateUserDTO testUser = new CreateUserDTO(firstname, lastname, username, password, email);
			userService.createUser(testUser);
		});
	}
	
	private static Stream<Arguments> invalidUsers(){
		return Stream.of(
				Arguments.of("","","","",""),
				Arguments.of(" ","  ","   ","    ","     "),
				Arguments.of("1","lastname","username","password","email@email.com"),
				Arguments.of("firstname","1","username","password","email@email.com"),
				Arguments.of("firstname","lastname","username","password","bad email"),
				Arguments.of("firstname","","username","password","bad email"),
				Arguments.of("firstname","lastname","","password","bad email"),
				Arguments.of("firstname","lastname","username","","bad email"),
				Arguments.of(null,null,null,null,null)
				);
	}
	
	@Test
	void testLogin_positive() throws NotFoundException, DatabaseExeption, BadInputException {
		User expectedUser = new User(1,"username", "password", "firstname", "lastname", "email@email.com", new UserRole(1, "customer"));
		LoginDTO testCredentials = new LoginDTO("username", "password");
		when(loginRepo.loginUser(testCredentials))
			.thenReturn(expectedUser);
		
		User actual = userService.login(testCredentials);
		
		assertEquals(expectedUser, actual);
	}
	
	@ParameterizedTest
	@MethodSource("invalidCredentials")
	void testLogin_negative(String username, String password) {
		Assertions.assertThrows(BadInputException.class, () -> {
			LoginDTO testCredentials = new LoginDTO(username, password);
			userService.login(testCredentials);
		});
	}
	
	private static Stream<Arguments> invalidCredentials(){
		return Stream.of(
				Arguments.of("","password"),
				Arguments.of("username",""),
				Arguments.of(null,null)
				);
	}
}
