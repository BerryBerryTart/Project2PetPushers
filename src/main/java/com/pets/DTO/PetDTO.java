package com.pets.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @EqualsAndHashCode @ToString
public class PetDTO {
	@NotBlank
	private String pet_name;
	@NotBlank
	private int pet_age;
	@NotBlank
	private String pet_species;
	@NotBlank
	private String pet_breed;
	@NotBlank
	private String pet_description;
	
	private String pet_type;

	//its optional
	private byte[] pet_image;
}
