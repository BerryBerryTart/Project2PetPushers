package com.pets.test.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import com.pets.model.User;
import com.pets.model.UserRole;

class TestUserModel {
	private int userId = 1;
	private String firstName = "firstName";
	private String lastName = "lastName";
	private String username = "username";
	private String password = "password";
	private String email = "email@email.com";
	private UserRole role = new UserRole(1, "customer");
	
	@Test
	void testUserEquals() {
		User expected = new User(userId, username, password, firstName, lastName, email, role);
		User actual = new User(userId, username, password, firstName, lastName, email, role);
	
		assertEquals(expected, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
	}
	
	@Test
	void testUserNotEquals() {
		User expected = new User(userId, username, password, firstName, lastName, email, role);
		User actual = new User(2, username, password, firstName, lastName, email, role);
	
		assertNotEquals(expected, actual);
		assertNotEquals(expected.hashCode(), actual.hashCode());
	}
}
