package com.pets.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @EqualsAndHashCode @ToString
public class UpdateAdoptionRequestDTO {
	@NotBlank
	String status;
	@NotBlank
	String reason;
}
