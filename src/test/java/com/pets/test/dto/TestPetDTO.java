package com.pets.test.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import com.pets.DTO.PetDTO;

class TestPetDTO {
	private String name = "name";
	private int age = 1;
	private String species = "species";
	private String breed = "breed";
	private String description = "description";
	private String type = "type";
	private byte[] image = "image".getBytes();
	
	@Test
	void testPetDTOEquals() {
		PetDTO expected = new PetDTO();
		expected.setPet_name(name);
		expected.setPet_age(age);
		expected.setPet_species(species);
		expected.setPet_breed(breed);
		expected.setPet_description(description);
		expected.setPet_type(type);
		expected.setPet_image(image);
		
		PetDTO actual = new PetDTO();
		actual.setPet_name(name);
		actual.setPet_age(age);
		actual.setPet_species(species);
		actual.setPet_breed(breed);
		actual.setPet_description(description);
		actual.setPet_type(type);
		actual.setPet_image(image);
		
		assertEquals(expected, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
		assertEquals(expected.toString(), actual.toString());
	}
	
	@Test
	void testPetDTONotEquals() {
		PetDTO expected = new PetDTO();
		expected.setPet_name(name);
		expected.setPet_age(age + 1);
		expected.setPet_species(species);
		expected.setPet_breed(breed);
		expected.setPet_description(description);
		expected.setPet_type(type);
		expected.setPet_image(image);
		
		PetDTO actual = new PetDTO();
		actual.setPet_name(name);
		actual.setPet_age(age);
		actual.setPet_species(species);
		actual.setPet_breed(breed);
		actual.setPet_description(description);
		actual.setPet_type(type);
		actual.setPet_image(image);
		
		assertNotEquals(expected, actual);
		assertNotEquals(expected.hashCode(), actual.hashCode());
		assertNotEquals(expected.toString(), actual.toString());
	}
}
