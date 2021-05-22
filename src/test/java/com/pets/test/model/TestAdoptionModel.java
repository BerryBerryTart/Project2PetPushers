package com.pets.test.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import com.pets.model.AdoptionRequest;
import com.pets.model.AdoptionRequestStatus;
import com.pets.model.Pet;
import com.pets.model.PetStatus;
import com.pets.model.PetType;
import com.pets.model.User;
import com.pets.model.UserRole;

class TestAdoptionModel {
	User user;
	Pet pet;
	//user bits
	private int userId = 1;
	private String firstName = "firstName";
	private String lastName = "lastName";
	private String username = "username";
	private String password = "password";
	private String email = "email@email.com";
	private UserRole role = new UserRole(1, "customer");
	
	//pet bits
	private int petId = 1;
	private String petName = "pet";
	private int petAge = 10;
	private String petSpecies = "species";
	private String petBreed = "breed";
	private String description = "description";
	private Date created = new Date();
	private byte[] image = "image".getBytes();
	private PetType type = new PetType(1, "real");
	private PetStatus status = new PetStatus(1, "unadopted");
	
	//adoption bits
	private int adoptionId = 1;
	private AdoptionRequestStatus requestStatus = new AdoptionRequestStatus(1, "approved");
	private String requestDescription = "req description";
	private String requestResponse = "req response";
	
	@BeforeTestClass
	void setup() {
		user = new User(userId, username, password, firstName, lastName, email, role);
		
		pet = new Pet();
		pet.setPet_age(petAge);
		pet.setPet_id(petId);
		pet.setPet_species(petSpecies);
		pet.setPet_name(petName);
		pet.setPet_breed(petBreed);
		pet.setPet_description(description);
		pet.setPet_list_date(created);
		pet.setPet_image(image);
		pet.setPet_type(type);
		pet.setPet_status(status);
	}
	
	@Test 
	void testAdoptionEquals() {
		AdoptionRequest expected = new AdoptionRequest();
		expected.setAdoption_request_user(user);
		expected.setAdoption_request_pet(pet);
		expected.setAdoption_request_id(adoptionId);
		expected.setAdoption_request_created(created);
		expected.setAdoption_request_resolved(created);
		expected.setAdoption_request_description(requestDescription);
		expected.setAdoption_request_response(requestResponse);
		expected.setAdoption_request_status(requestStatus);
		
		AdoptionRequest actual = new AdoptionRequest();
		actual.setAdoption_request_user(user);
		actual.setAdoption_request_pet(pet);
		actual.setAdoption_request_id(adoptionId);
		actual.setAdoption_request_created(created);
		actual.setAdoption_request_resolved(created);
		actual.setAdoption_request_description(requestDescription);
		actual.setAdoption_request_response(requestResponse);
		actual.setAdoption_request_status(requestStatus);
		
		assertEquals(expected, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
	}
	
	@Test 
	void testAdoptionNotEquals() {
		AdoptionRequest expected = new AdoptionRequest();
		expected.setAdoption_request_user(user);
		expected.setAdoption_request_pet(pet);
		expected.setAdoption_request_id(adoptionId + 1);
		expected.setAdoption_request_created(created);
		expected.setAdoption_request_resolved(created);
		expected.setAdoption_request_description(requestDescription);
		expected.setAdoption_request_response(requestResponse);
		expected.setAdoption_request_status(requestStatus);
		
		AdoptionRequest actual = new AdoptionRequest();
		actual.setAdoption_request_user(user);
		actual.setAdoption_request_pet(pet);
		actual.setAdoption_request_id(adoptionId);
		actual.setAdoption_request_created(created);
		actual.setAdoption_request_resolved(created);
		actual.setAdoption_request_description(requestDescription);
		actual.setAdoption_request_response(requestResponse);
		actual.setAdoption_request_status(requestStatus);
		
		assertNotEquals(expected, actual);
		assertNotEquals(expected.hashCode(), actual.hashCode());
	}
}
