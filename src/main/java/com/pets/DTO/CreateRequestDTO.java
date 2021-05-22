package com.pets.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CreateRequestDTO {
	@NotBlank
	private int petId;
	@NotBlank
	private String description;
	
}
