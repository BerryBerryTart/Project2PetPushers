package com.pets.test.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.pets.model.Pet;
import com.pets.model.PetStatus;
import com.pets.model.PetType;

class TestPetModel {
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
	
	@Test
	void testPetModelSuccess() {		
		Pet actual = new Pet();
		actual.setPet_age(petAge);
		actual.setPet_id(petId);
		actual.setPet_species(petSpecies);
		actual.setPet_name(petName);
		actual.setPet_breed(petBreed);
		actual.setPet_description(description);
		actual.setPet_list_date(created);
		actual.setPet_image(image);
		actual.setPet_type(type);
		actual.setPet_status(status);
		
		Pet expected = new Pet();
		expected.setPet_age(petAge);
		expected.setPet_id(petId);
		expected.setPet_species(petSpecies);
		expected.setPet_name(petName);
		expected.setPet_breed(petBreed);
		expected.setPet_description(description);
		expected.setPet_list_date(created);
		expected.setPet_image(image);
		expected.setPet_type(type);
		expected.setPet_status(status);
		
		assertEquals(expected, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
	}

	@Test
	void testPetModelNotEqual() {		
		Pet actual = new Pet();
		actual.setPet_age(petAge);
		actual.setPet_id(petId);
		actual.setPet_species(petSpecies);
		actual.setPet_name(petName);
		actual.setPet_breed(petBreed);
		actual.setPet_description(description);
		actual.setPet_list_date(created);
		actual.setPet_image(image);
		actual.setPet_type(type);
		actual.setPet_status(status);
		
		Pet expected = new Pet();
		expected.setPet_age(2);
		expected.setPet_id(petId);
		expected.setPet_species(petSpecies);
		expected.setPet_name(petName);
		expected.setPet_breed(petBreed);
		expected.setPet_description(description);
		expected.setPet_list_date(created);
		expected.setPet_image(image);
		expected.setPet_type(type);
		expected.setPet_status(status);
		
		assertNotEquals(expected, actual);
		assertNotEquals(expected.hashCode(), actual.hashCode());
	}
}
