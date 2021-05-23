package com.pets.test.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import com.pets.DTO.LoginDTO;

class TestLoginDTO {
	private String username = "username";
	private String password = "password";
	
	@Test
	void testLoginDTOEquals(){
		LoginDTO expected = new LoginDTO();
		expected.setUsername(username);
		expected.setPassword(password);
		
		LoginDTO actual = new LoginDTO();
		actual.setUsername(username);
		actual.setPassword(password);
		
		assertEquals(expected, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
		assertEquals(expected.toString(), actual.toString());
	}
	
	@Test
	void testLoginDTONotEquals(){
		LoginDTO expected = new LoginDTO();
		expected.setUsername("not UserName");
		expected.setPassword(password);
		
		LoginDTO actual = new LoginDTO();
		actual.setUsername(username);
		actual.setPassword(password);
		
		assertNotEquals(expected, actual);
		assertNotEquals(expected.hashCode(), actual.hashCode());
		assertNotEquals(expected.toString(), actual.toString());
	}
}
