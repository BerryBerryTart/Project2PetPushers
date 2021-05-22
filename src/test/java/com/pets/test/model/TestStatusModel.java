package com.pets.test.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import com.pets.model.AdoptionRequestStatus;
import com.pets.model.PetStatus;
import com.pets.model.PetType;
import com.pets.model.UserRole;

class TestStatusModel {
	
	//PET STATUS MODEL TEST
	
	@Test
	void testPetStatusEquals() {
		PetStatus expected = new PetStatus(1, "unadopted");
		PetStatus actual = new PetStatus(1, "unadopted");
		
		assertEquals(expected, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
	}
	
	@Test
	void testPetStatusNotEquals() {
		PetStatus expected = new PetStatus(2, "unadopted");
		PetStatus actual = new PetStatus(1, "unadopted");
		
		assertNotEquals(expected, actual);
		assertNotEquals(expected.hashCode(), actual.hashCode());
	}
	
	//PET TYPE MODEL TEST
	
	@Test 
	void testPetTypeEquals(){
		PetType expected = new PetType(2, "real");
		PetType actual = new PetType(1, "real");
		
		assertNotEquals(expected, actual);
		assertNotEquals(expected.hashCode(), actual.hashCode());
	}
	
	@Test 
	void testPetTypeNotEquals(){
		PetType expected = new PetType(1, "real");
		PetType actual = new PetType(1, "real");
		
		assertEquals(expected, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
	}
	
	//USER ROLE MODEL TEST
	
	@Test 
	void testUserRoleEquals(){
		UserRole expected = new UserRole(1, "customer");
		UserRole actual = new UserRole(1, "customer");
		
		assertEquals(expected, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
	}
	
	@Test 
	void testUserRoleNotEquals(){
		UserRole expected = new UserRole(2, "customer");
		UserRole actual = new UserRole(1, "customer");
		
		assertNotEquals(expected, actual);
		assertNotEquals(expected.hashCode(), actual.hashCode());
	}
	
	//ADOPTION REQUEST MODEL TEST
	
	@Test
	void testAdoptStatusEquals() {
		AdoptionRequestStatus expected = new AdoptionRequestStatus(1, "approved");
		AdoptionRequestStatus actual = new AdoptionRequestStatus(1, "approved");
		
		assertEquals(expected, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
	}
	
	@Test
	void testAdoptStatusNotEquals() {
		AdoptionRequestStatus expected = new AdoptionRequestStatus(2, "approved");
		AdoptionRequestStatus actual = new AdoptionRequestStatus(1, "approved");
		
		assertNotEquals(expected, actual);
		assertNotEquals(expected.hashCode(), actual.hashCode());
	}
	
}
