package com.pets.test.loginDAO;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.pets.DAO.LoginRepo;
import com.pets.DAO.EnumeratedRepo;
import com.pets.DTO.CreateUserDTO;
import com.pets.DTO.LoginDTO;
import com.pets.exception.CreationException;
import com.pets.exception.DatabaseExeption;
import com.pets.exception.NotFoundException;
import com.pets.model.User;
import com.pets.model.UserRole;

@ExtendWith(SpringExtension.class)
@ContextHierarchy({
	@ContextConfiguration(locations = "classpath:applicationContext.xml"),
	@ContextConfiguration(locations = "classpath:dispatcherContext.xml")
})
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //lets us use before all as non static
class testLoginDAO {

	@Autowired
	private EnumeratedRepo enumRepo;
	
	@Autowired
	private LoginRepo loginRepo;
	
	@BeforeAll
	@Transactional
	@Commit
	void enumerateTable() {
		UserRole role = enumRepo.createUserRole("customer");
		
		assertTrue(role.getUser_role_id() != 0);
	}
	
	@Test
	@Transactional
	@Order(1)
	@Commit // or @Rollback(false), functionally the same
	void testAddUser_hasAutoGeneratedId() throws CreationException, DatabaseExeption {
		CreateUserDTO createUserDTO = new CreateUserDTO("firstName", "lastName", "username", "password", "email");
		User user = new User();
		
		
		user = loginRepo.createUser(createUserDTO);
		
		
		System.out.println(user);
		
		assertTrue(user.getUser_id() != 0);
		assertTrue(user.getUser_role().getUser_role().equals("customer"));
	}
	
	@Test
	@Transactional
	@Order(2)
	void testUserAlreadyExists() {
		CreateUserDTO createUserDTO = new CreateUserDTO("firstName", "lastName", "username", "password", "email");
			
		Assertions.assertThrows(CreationException.class, () -> {
			loginRepo.createUser(createUserDTO);
		});
	}
	
	@Test
	@Transactional
	@Order(3)
	void testUserCanLogin() throws NotFoundException, DatabaseExeption {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setUsername("username");
		loginDTO.setPassword("password");
		
		User user = loginRepo.loginUser(loginDTO);
		
		assertNotNull(user);
	}
	
	@Test
	@Transactional
	@Order(4)
	void testUserLoginDoesNotExist() {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setUsername("notFound");
		loginDTO.setPassword("notFound");
		
		Assertions.assertThrows(NotFoundException.class, () -> {
			loginRepo.loginUser(loginDTO);
		});
	}

}
