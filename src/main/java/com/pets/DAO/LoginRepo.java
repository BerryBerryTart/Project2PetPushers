package com.pets.DAO;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pets.DTO.CreateUserDTO;
import com.pets.DTO.LoginDTO;
import com.pets.exception.CreationException;
import com.pets.exception.DatabaseExeption;
import com.pets.exception.NotFoundException;
import com.pets.model.User;
import com.pets.model.UserRole;

@Repository
public class LoginRepo {
	private static Logger logger = LoggerFactory.getLogger(LoginRepo.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public User createUser(CreateUserDTO createUserDTO) throws CreationException, DatabaseExeption {
		Session session = sessionFactory.getCurrentSession();
		User user = new User();
		boolean userExistsAlready = true;
		
		String hql = "FROM User u WHERE u.username = :username AND u.email = :email";
		try {
			Query query = session.createQuery(hql);
			query.setParameter("username", createUserDTO.getUsername());
			query.setParameter("email", createUserDTO.getEmail());
			user = (User) query.getSingleResult();
		} catch (NoResultException e) {
			userExistsAlready = false;
		}
		
		if (userExistsAlready) {
			logger.error("Username/Email Already Exists");
			throw new CreationException("Username/Email Already Exists");
		}
		
		UserRole role = session.load(UserRole.class, 1);

		user.setFirst_name(createUserDTO.getFirst_name());
		user.setLast_name(createUserDTO.getLast_name());
		user.setEmail(createUserDTO.getEmail());
		user.setUsername(createUserDTO.getUsername());
		user.setPassword(hashPassword(createUserDTO.getPassword()));
		user.setUser_role(role);
		session.persist(user);
		
		return user;
	}

	@Transactional
	public User loginUser(LoginDTO loginDTO) throws NotFoundException, DatabaseExeption {
		Session session = sessionFactory.getCurrentSession();
		
		User user = null;
		String hql = "FROM User u WHERE u.username = :username AND u.password = :password";
		
		try {
			Query query = session.createQuery(hql);
			query.setParameter("username", loginDTO.getUsername());
			query.setParameter("password", hashPassword(loginDTO.getPassword()));
			user = (User) query.getSingleResult();		
		} catch (NoResultException e) {
			logger.error("User DNE");
			throw new NotFoundException("User Not Found");
		}
		
		return user;
	}
	
	private static String hashPassword(String s) throws DatabaseExeption {
		String hashed = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(s.getBytes(StandardCharsets.UTF_8));
			byte[] digest = md.digest();
			hashed = String.format("%064x", new BigInteger(1, digest));
		} catch (NoSuchAlgorithmException e) {
			throw new DatabaseExeption(e.getMessage());
		}
		
		if (hashed == null) {
			throw new DatabaseExeption("Password Encryption Error");
		}
		
		return hashed;
	}
}
