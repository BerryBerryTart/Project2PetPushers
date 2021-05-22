package com.pets.test.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import com.pets.DTO.CreateRequestDTO;

class TestCreateRequestDTO {
	private String description = "description";
	private int id = 1;
	
	@Test
	void testCreateRequestDTOEquals() {
		CreateRequestDTO expected = new CreateRequestDTO();
		expected.setPetId(id);
		expected.setDescription(description);
		
		CreateRequestDTO actual = new CreateRequestDTO();
		actual.setPetId(id);
		actual.setDescription(description);
		
		assertEquals(expected, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
		assertEquals(expected.toString(), actual.toString());
	}
	
	@Test
	void testCreateRequestDTONotEquals() {
		CreateRequestDTO expected = new CreateRequestDTO();
		expected.setPetId(id + 1);
		expected.setDescription(description);
		
		CreateRequestDTO actual = new CreateRequestDTO();
		actual.setPetId(id);
		actual.setDescription(description);
		
		assertNotEquals(expected, actual);
		assertNotEquals(expected.hashCode(), actual.hashCode());
		assertNotEquals(expected.toString(), actual.toString());
	}
}
