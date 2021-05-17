package com.pets.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pets.DAO.UserDAO;
import com.pets.exception.NoResultException;
import com.pets.exception.UserNotFoundException;
import com.pets.model.User;

@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;
	
	@Transactional(rollbackFor = { UserNotFoundException.class })
	public User login(String username, String password) throws UserNotFoundException, NoSuchAlgorithmException {
		
		
		
		String hashedPassword = hashPassword(password);
		try {
			User user = userDAO.getUserByCredentials(username, hashedPassword);
			// A simple print out to test if password was hashed
			System.out.println("Given password: " + password 
					+ "\nHashed password: " + hashedPassword);
			return user;
		} catch (NoResultException e) {
			throw new UserNotFoundException("User not found w/ the provided username and password");
		}
		
	}

	private String hashPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
	    byte[] digest = md.digest();
	    String passwordHashed = DatatypeConverter.printHexBinary(digest).toUpperCase();
		return passwordHashed;
	}
}
