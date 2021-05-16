package com.pets.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @EqualsAndHashCode
public class LoginDTO {
	@NotBlank
	private String username;
	@NotBlank
	private String password;		
}
