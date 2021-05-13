package com.pets.DAO;

import org.springframework.stereotype.Repository;

import com.pets.exception.NoResultException;
import com.pets.model.User;

import lombok.NoArgsConstructor;

// A fake DAO that returns a fake User if username matches or an exception if it doesn't
@NoArgsConstructor
@Repository
public class UserDAO {
	public User getUserByCredentials(String username, String hashedPassword) throws NoResultException {
		if (username == "User1") {
			return new User(username, hashedPassword);
		} else {
			throw new NoResultException();
		}
	}
}
