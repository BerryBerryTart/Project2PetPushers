package com.pets.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pets.DAO.LoginRepo;
import com.pets.DTO.CreateUserDTO;
import com.pets.DTO.LoginDTO;
import com.pets.exception.BadInputException;
import com.pets.exception.CreationException;
import com.pets.exception.DatabaseExeption;
import com.pets.exception.NotFoundException;
import com.pets.model.User;

import lombok.NoArgsConstructor;

@Service @NoArgsConstructor
public class UserService {
	private static Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private LoginRepo loginRepo;
	
	// Constructor for mock insertion
	public UserService(LoginRepo loginRepo) {
		super();
		this.loginRepo = loginRepo;
	}
	
	@Transactional(rollbackFor = { NotFoundException.class, BadInputException.class})
	public User login(LoginDTO loginDTO) throws NotFoundException, DatabaseExeption, BadInputException {
		logger.info("UserService.login() executed.");
		//Check if username or password is blank
		if (loginDTO.getUsername() == null || loginDTO.getUsername().trim() == "") {
			throw new BadInputException("Username cannot be blank.");
		}
		if (loginDTO.getPassword() == null || loginDTO.getPassword().trim() == "") {
			throw new BadInputException("Password cannot be blank.");
		}
		return loginRepo.loginUser(loginDTO);
	}
	
	@Transactional(rollbackFor = {CreationException.class, BadInputException.class})
	public User createUser(CreateUserDTO inputUser) throws BadInputException, CreationException, DatabaseExeption {
		logger.info("UserService.createUser executed.");
		//check if all fields in CreateUSerDTO are valid using regex
		if (inputUser.getFirst_name() == null || inputUser.getFirst_name().trim() == "") {
			throw new BadInputException("Firstname cannot be blank.");
		}
		if (inputUser.getLast_name() == null || inputUser.getLast_name().trim() == "") {
			throw new BadInputException("Lastname cannot be blank.");
		}
		if (!(inputUser.getFirst_name().trim().matches("^[a-zA-Z']+$"))
				|| !(inputUser.getLast_name().trim().matches("^[a-zA-Z']+$"))) {
			
			throw new BadInputException("User full name must contain only letters. User entered: "
					+ inputUser.getFirst_name() + " " + inputUser.getLast_name());
		}
		if (inputUser.getUsername() == null || inputUser.getUsername().trim() == "") {
			throw new BadInputException("Username cannot be blank.");
		}
		if (inputUser.getPassword() == null || inputUser.getPassword().trim() == "") {
			throw new BadInputException("Password cannot be blank.");
		}
		if (!inputUser.getEmail().trim().matches("^(.+)@(.+)$")) {
			throw new BadInputException("Please enter a valid email. User entered:" + inputUser.getEmail());
		}
		return loginRepo.createUser(inputUser);
	}
}
