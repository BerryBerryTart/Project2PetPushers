package com.pets.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pets.DTO.LoginDTO;
import com.pets.DTO.MessageDTO;
import com.pets.exception.UserNotFoundException;
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
	
	@PostMapping(path = "/login_account")
	public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO) throws NoSuchAlgorithmException {
		try {
			User user = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
			HttpSession session = request.getSession(true);
			
			// For now sessionAttribute, TODO look into JWT's
			session.setAttribute("loggedInUser", user);
			
			// Not sure if I should return user object but just in case
			return ResponseEntity.status(200).body(user);
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(400).build();
		}
	}
	
	@GetMapping(path = "/logout_account")
	public ResponseEntity<Object> logout() {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("loggedInUser") == null) {
			return ResponseEntity.status(401)
					.body(new MessageDTO("You must be logged in to access this resource"));
		}else {
			session.invalidate();
			return ResponseEntity.status(201).build();
		}
	}
	
	//test endpoint
	@GetMapping(path = "test")
	public @ResponseBody String ourFirstEndpoint() {
		return "Test";
	}
}
