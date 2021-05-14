package com.pets.DAO;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berry.DTO.CreateUserDTO;
import com.berry.DTO.LoginDTO;
import com.berry.app.Application;
import com.berry.exception.CreationException;
import com.berry.exception.DatabaseExeption;
import com.berry.exception.NotFoundException;
import com.berry.model.Role;
import com.berry.model.Users;
import com.berry.util.SessionUtility;

public class LoginRepo {
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public Users createUser(CreateUserDTO createUserDTO) throws CreationException, DatabaseExeption {
		Session session = SessionUtility.getSession();
		Users user = null;
		boolean userExistsAlready = true;
		
		String hql = "FROM Users u WHERE u.username = :username AND u.email = :email";
		try {
			Query query = session.createQuery(hql);
			query.setParameter("username", createUserDTO.getUsername());
			query.setParameter("email", createUserDTO.getEmail());
			user = (Users) query.getSingleResult();
		} catch (NoResultException e) {
			userExistsAlready = false;
		}
		
		if (userExistsAlready == true) {
			logger.error("Username/Email Already Exists");
			throw new CreationException("Username/Email Already Exists");
		}
		
		Role role = session.load(Role.class, 1);
		
		session.beginTransaction();
		user = new Users();
		user.setFirst_name(createUserDTO.getFirst_name());
		user.setLast_name(createUserDTO.getLast_name());
		user.setEmail(createUserDTO.getEmail());
		user.setUsername(createUserDTO.getUsername());
		user.setPassword(hashPassword(createUserDTO.getPassword()));
		user.setRole_id(role);
		session.save(user);
		
		session.getTransaction().commit();
		
		if (session != null) {
			session.close();
		}
		
		return user;
	}

	public Users loginUser(LoginDTO loginDTO) throws NotFoundException, DatabaseExeption {
		Session session = SessionUtility.getSession();
		
		Users user = null;
		String hql = "FROM Users u WHERE u.username = :username AND u.password = :password";
		
		try {
			Query query = session.createQuery(hql);
			query.setParameter("username", loginDTO.getUsername());
			query.setParameter("password", hashPassword(loginDTO.getPassword()));
			user = (Users) query.getSingleResult();		
		} catch (NoResultException e) {
			logger.error("User DNE");
			throw new NotFoundException("User Not Found");
		} finally {
			if (session != null) {
				session.close();
			}			
		}
		
		return user;
	}
	
	private String hashPassword(String s) throws DatabaseExeption {
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
