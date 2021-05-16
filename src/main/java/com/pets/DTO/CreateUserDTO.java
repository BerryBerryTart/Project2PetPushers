package com.pets.DTO;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @EqualsAndHashCode
public class CreateUserDTO {
	private String first_name;
	private String last_name;
	private String username;
	private String password;
	private String email;
}
