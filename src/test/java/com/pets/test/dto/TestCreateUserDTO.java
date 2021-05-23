package com.pets.test.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import com.pets.DTO.CreateUserDTO;
import com.pets.model.User;
import com.pets.model.UserRole;

class TestCreateUserDTO {
	private String firstName = "firstName";
	private String lastName = "lastName";
	private String username = "username";
	private String password = "password";
	private String email = "email@email.com";
	
	@Test
	void testUserEquals() {
		CreateUserDTO expected = new CreateUserDTO();
		expected.setEmail(email);
		expected.setFirst_name(firstName);
		expected.setLast_name(lastName);
		expected.setUsername(username);
		expected.setPassword(password);
		
		CreateUserDTO actual = new CreateUserDTO();
		actual.setEmail(email);
		actual.setFirst_name(firstName);
		actual.setLast_name(lastName);
		actual.setUsername(username);
		actual.setPassword(password);
		
		assertEquals(expected, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
	}
	
	@Test
	void testUserNotEquals() {
		CreateUserDTO expected = new CreateUserDTO();
		expected.setEmail("not email");
		expected.setFirst_name(firstName);
		expected.setLast_name(lastName);
		expected.setUsername(username);
		expected.setPassword(password);
		
		CreateUserDTO actual = new CreateUserDTO();
		actual.setEmail(email);
		actual.setFirst_name(firstName);
		actual.setLast_name(lastName);
		actual.setUsername(username);
		actual.setPassword(password);
	
		assertNotEquals(expected, actual);
		assertNotEquals(expected.hashCode(), actual.hashCode());
	}
}
