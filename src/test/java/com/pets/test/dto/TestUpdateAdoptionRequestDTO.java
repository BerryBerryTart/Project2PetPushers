package com.pets.test.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import com.pets.DTO.CreateRequestDTO;
import com.pets.DTO.UpdateAdoptionRequestDTO;

class TestUpdateAdoptionRequestDTO {
	private String reason = "reason";
	private String status= "approved";
	
	@Test
	void testUpdateRequestDTOEquals() {
		UpdateAdoptionRequestDTO expected = new UpdateAdoptionRequestDTO();
		expected.setReason(reason);
		expected.setStatus(status);
		
		UpdateAdoptionRequestDTO actual = new UpdateAdoptionRequestDTO();
		actual.setReason(reason);
		actual.setStatus(status);
		
		assertEquals(expected, actual);
		assertEquals(expected.hashCode(), actual.hashCode());
		assertEquals(expected.toString(), actual.toString());
	}
	
	@Test
	void testUpdateRequestDTONotEquals() {
		UpdateAdoptionRequestDTO expected = new UpdateAdoptionRequestDTO();
		expected.setReason(reason);
		expected.setStatus("rejected");
		
		UpdateAdoptionRequestDTO actual = new UpdateAdoptionRequestDTO();
		actual.setReason(reason);
		actual.setStatus(status);
		
		assertNotEquals(expected, actual);
		assertNotEquals(expected.hashCode(), actual.hashCode());
		assertNotEquals(expected.toString(), actual.toString());
	}
}
