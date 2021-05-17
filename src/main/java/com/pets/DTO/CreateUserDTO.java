package com.pets.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @EqualsAndHashCode
public class CreateUserDTO {	
	@NotBlank
	private String first_name;
	@NotBlank
	private String last_name;
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	private String email;
}
