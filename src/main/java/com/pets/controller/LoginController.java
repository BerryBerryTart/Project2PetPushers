package com.pets.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pets.DTO.CreateUserDTO;
import com.pets.DTO.LoginDTO;
import com.pets.DTO.MessageDTO;
import com.pets.exception.BadInputException;
import com.pets.exception.CreationException;
import com.pets.exception.DatabaseExeption;
import com.pets.exception.NotFoundException;
import com.pets.model.User;
import com.pets.service.UserService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class LoginController {
	@Autowired
	private UserService userService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;

	@PostMapping(path = "login_account")
	@ResponseStatus(HttpStatus.OK)
	public User login(@RequestBody LoginDTO loginDTO) throws DatabaseExeption {
		try {
			User user = userService.login(loginDTO);
			HttpSession session = request.getSession(true);

			// For now sessionAttribute, TODO look into JWT's
			session.setAttribute("loggedInUser", user);

			return user;
		} catch (BadInputException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PostMapping(path = "register_account")
	@ResponseStatus(code = HttpStatus.CREATED)
	public User addUser(@RequestBody CreateUserDTO createUserDTO) {
		User user;
		try {
			user = userService.createUser(createUserDTO);
			return user;
		} catch (BadInputException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (CreationException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (DatabaseExeption e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something happened in the database.");
		}
	}

	@GetMapping(path = "logout_account")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void logout() {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("loggedInUser") == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must be logged in to log out.");
		} else {
			session.invalidate();
		}
	}
	
	//test endpoint
	@GetMapping(path = "test")
	@ResponseStatus(code = HttpStatus.OK)
	public MessageDTO ourFirstEndpoint() {
		return new MessageDTO("Thanks for GET request!!!");
	}
}
