package com.pets.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class CreateRequestDTO {
	@NotBlank
	private int petId;
	@NotBlank
	private String description;
	
}
