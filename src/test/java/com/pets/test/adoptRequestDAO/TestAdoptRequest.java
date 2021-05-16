package com.pets.test.adoptRequestDAO;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.pets.DAO.AdoptionRequestRepo;
import com.pets.DAO.EnumeratedRepo;
import com.pets.DAO.LoginRepo;
import com.pets.DAO.PetRepo;
import com.pets.DTO.CreateUserDTO;
import com.pets.DTO.LoginDTO;
import com.pets.DTO.PetDTO;
import com.pets.exception.CreationException;
import com.pets.exception.DatabaseExeption;
import com.pets.exception.NotFoundException;
import com.pets.model.AdoptionRequest;
import com.pets.model.AdoptionRequestStatus;
import com.pets.model.Pet;
import com.pets.model.PetStatus;
import com.pets.model.PetType;
import com.pets.model.User;
import com.pets.model.UserRole;

@ExtendWith(SpringExtension.class)
@ContextHierarchy({ @ContextConfiguration(locations = "classpath:applicationContext.xml"),
		@ContextConfiguration(locations = "classpath:dispatcherContext.xml") })
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // lets us use before all as non static
@DirtiesContext(classMode = ClassMode.AFTER_CLASS) // clean up tables after
public class TestAdoptRequest {
	@Autowired
	private EnumeratedRepo enumRepo;

	@Autowired
	private LoginRepo loginRepo;

	@Autowired
	private PetRepo petRepo;

	@Autowired
	private AdoptionRequestRepo requestRepo;

	@BeforeAll
	@Transactional
	@Commit
	void enumerateTable() {
		// create dummy user roles for testing
		UserRole role1 = enumRepo.createUserRole("customer");
		UserRole role2 = enumRepo.createUserRole("manager");
		assertTrue(role1.getUser_role_id() != 0);
		assertTrue(role2.getUser_role_id() != 0);

		// create dummy pet types
		PetType petType1 = enumRepo.createPetType("real");
		PetType petType2 = enumRepo.createPetType("digital");
		assertTrue(petType1.getPet_type_id() != 0);
		assertTrue(petType2.getPet_type_id() != 0);

		// create dummy pet status'
		PetStatus petStatus1 = enumRepo.createPetStatus("unadopted");
		PetStatus petStatus2 = enumRepo.createPetStatus("adopted");
		assertTrue(petStatus1.getPet_status_id() != 0);
		assertTrue(petStatus2.getPet_status_id() != 0);

		// create dummy request status'
		AdoptionRequestStatus reqStatus1 = enumRepo.createAdoptionStatus("pending");
		AdoptionRequestStatus reqStatus2 = enumRepo.createAdoptionStatus("approved");
		AdoptionRequestStatus reqStatus3 = enumRepo.createAdoptionStatus("rejected");
		assertTrue(reqStatus1.getAdoption_request_status_id() != 0);
		assertTrue(reqStatus2.getAdoption_request_status_id() != 0);
		assertTrue(reqStatus3.getAdoption_request_status_id() != 0);
	}

	@Test
	@Transactional
	@Order(0)
	@Commit // or @Rollback(false), functionally the same
	void testAddAdoptRequest_hasAutoGeneratedId() throws CreationException, DatabaseExeption {
		// Create dummy user 1
		CreateUserDTO createUser1DTO = new CreateUserDTO("firstName", "lastName", "username", "password", "email");
		User user1 = loginRepo.createUser(createUser1DTO);
		assertTrue(user1.getUser_id() != 0);

		// create dummy user 2
		CreateUserDTO createUser2DTO = new CreateUserDTO("new_firstName", "new_lastName", "new_username",
				"new_password", "new_email");
		User user2 = loginRepo.createUser(createUser2DTO);
		assertTrue(user2.getUser_id() != 0);

		// create dummy pet 1
		PetDTO petDto1 = new PetDTO();
		petDto1.setPet_age(1);
		petDto1.setPet_name("pet_name");
		petDto1.setPet_type("real");
		petDto1.setPet_breed("pet_breed");
		petDto1.setPet_species("pet_species");
		petDto1.setPet_description("pet_description");
		petDto1.setPet_image("image".getBytes());
		Pet pet1 = petRepo.createPet(petDto1);
		assertTrue(pet1.getPet_id() != 0);

		// create dummy pet 2
		PetDTO petDto2 = new PetDTO();
		petDto2.setPet_age(1);
		petDto2.setPet_name("pet_name");
		petDto2.setPet_type("real");
		petDto2.setPet_breed("pet_breed");
		petDto2.setPet_species("pet_species");
		petDto2.setPet_description("pet_description");
		petDto2.setPet_image("image".getBytes());
		Pet pet2 = petRepo.createPet(petDto2);
		assertTrue(pet2.getPet_id() != 0);

		// create adoption request 1
		String requestDescription1 = "pet request description";
		AdoptionRequest adoptRequest1 = requestRepo.createAdoptionRequest(user1, pet1, requestDescription1);
		
		//try to make a request for pet 1 again
		//should throw an error
		Assertions.assertThrows(CreationException.class, () -> {
			requestRepo.createAdoptionRequest(user1, pet1, requestDescription1);
		});
		
		// create adoption request 2 for later
		String requestDescription2 = "pet request description";
		requestRepo.createAdoptionRequest(user1, pet2, requestDescription2);

		// check if ID was generated
		assertTrue(adoptRequest1.getAdoption_request_id() != 0);
		// check if status is pending on generation
		assertTrue(adoptRequest1.getAdoption_request_status().getAdoption_request_status().equals("pending"));
		// check that created timestamp is not null
		assertNotNull(adoptRequest1.getAdoption_request_created());
		// ensure resolved timestamp was not automatically generated
		assertNull(adoptRequest1.getAdoption_request_resolved());
	}

	@Test
	@Transactional
	@Order(1)
	void testGetAllAdoptions() throws NotFoundException, DatabaseExeption {
		User user1 = loginRepo.loginUser(new LoginDTO("username", "password"));
		assertNotNull(user1);

		List<AdoptionRequest> petList = requestRepo.getAllAdoptionRequest(user1);

		// pet list SHOULD have more at least one pet in it
		assertTrue(petList.size() != 0);
	}

	@Test
	@Transactional
	@Order(3)
	void testGetAllAdoptions_userHasNone() throws NotFoundException, DatabaseExeption {
		User user2 = loginRepo.loginUser(new LoginDTO("new_username", "new_password"));
		assertNotNull(user2);

		List<AdoptionRequest> petList = requestRepo.getAllAdoptionRequest(user2);

		// pet list SHOULD have more at least one pet in it
		assertTrue(petList.size() == 0);
	}

	@Test
	@Transactional
	@Order(4)
	@Commit
	void testUserAdoptsDigitalPet() throws NotFoundException, DatabaseExeption, CreationException {
		// create dummy digital pet
		PetDTO petDto = new PetDTO();
		petDto.setPet_age(1);
		petDto.setPet_name("digital pet_name");
		petDto.setPet_type("digital");
		petDto.setPet_breed("digital pet_breed");
		petDto.setPet_species("digital pet_species");
		petDto.setPet_description("digital pet_description");
		petDto.setPet_image("image".getBytes());
		Pet pet = petRepo.createPet(petDto);
		assertTrue(pet.getPet_id() != 0);

		// log in a user
		User user = loginRepo.loginUser(new LoginDTO("username", "password"));
		assertNotNull(user);

		AdoptionRequest adoptRequest = requestRepo.createAdoptionRequest(user, pet, "digital pet request description");
		// check request status. should be approved on creation
		assertTrue(adoptRequest.getAdoption_request_status().getAdoption_request_status().equals("approved"));
	}

}