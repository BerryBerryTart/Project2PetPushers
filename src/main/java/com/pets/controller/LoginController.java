package com.pets.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.pets.DTO.CreateUserDTO;
import com.pets.DTO.LoginDTO;
import com.pets.DTO.MessageDTO;
import com.pets.exception.BadInputException;
import com.pets.exception.DatabaseExeption;
import com.pets.exception.NotFoundException;
import com.pets.model.User;
import com.pets.service.UserService;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Controller
public class LoginController {
	@Autowired
	private UserService userService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;

	// Constructor for mock tests
	public LoginController(UserService userService) {
		super();
		this.userService = userService;
	}

	@PostMapping(path = "login_account")
	public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO) throws DatabaseExeption {
		try {
			User user = userService.login(loginDTO);
			HttpSession session = request.getSession(true);

			// For now sessionAttribute, TODO look into JWT's
			session.setAttribute("loggedInUser", user);

			// Not sure if I should return user object but just in case
			return ResponseEntity.status(200).body(user);
		} catch (BadInputException e) {
			return ResponseEntity.status(400).body(new MessageDTO(e.getMessage()));
		} catch (NotFoundException e) {
			return ResponseEntity.status(404).body(new MessageDTO(e.getMessage()));
		}
	}

	@PostMapping(path = "register_account")
	public ResponseEntity<Object> addUser(@RequestBody CreateUserDTO createUserDTO) {
		User user;
		try {
			user = userService.createUser(createUserDTO);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new MessageDTO(e.getMessage()));
		}
		return ResponseEntity.status(201).body(user);
	}

	@GetMapping(path = "logout_account")
	public ResponseEntity<Object> logout() {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("loggedInUser") == null) {
			return ResponseEntity.status(401).body(new MessageDTO("You must be logged in to be able to logout"));
		} else {
			session.invalidate();
			return ResponseEntity.status(204).build();
		}
	}

	@GetMapping(path = "test")
	public ResponseEntity<Object> test() {
		return ResponseEntity.status(200).body("Test");
	}
}
