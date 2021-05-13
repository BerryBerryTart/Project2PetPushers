package com.pets.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// A fake user model to fill in for a return object
@Data @NoArgsConstructor @AllArgsConstructor
public class User {
	private String username;
	private String hashedPassword;
}
