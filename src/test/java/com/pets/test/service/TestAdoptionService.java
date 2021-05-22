package com.pets.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pets.DAO.AdoptionRequestRepo;
import com.pets.DAO.PetRepo;
import com.pets.DTO.CreateRequestDTO;
import com.pets.DTO.UpdateAdoptionRequestDTO;
import com.pets.exception.BadInputException;
import com.pets.exception.CreationException;
import com.pets.exception.NotAuthorizedException;
import com.pets.exception.NotFoundException;
import com.pets.exception.UpdateException;
import com.pets.model.AdoptionRequest;
import com.pets.model.Pet;
import com.pets.model.User;
import com.pets.model.UserRole;
import com.pets.service.AdoptionService;

@ExtendWith(MockitoExtension.class)
class TestAdoptionService {
	@Mock
	AdoptionRequestRepo adoptionRepo;
	@Mock
	PetRepo petRepo;
	@InjectMocks
	AdoptionService adoptionService;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testCreateAdoptionRequest_positive() throws CreationException, NotFoundException, BadInputException {
		AdoptionRequest expected = new AdoptionRequest();
		User testUser = new User();
		Pet testPet = new Pet();
		testPet.setPet_name("test");
		String testDescription = "description";
		CreateRequestDTO testRequest = new CreateRequestDTO(1, testDescription);
		
		when(petRepo.getPetById(1)).thenReturn(testPet);
		when(adoptionRepo.createAdoptionRequest(testUser, testPet, testDescription)).thenReturn(expected);
		
		AdoptionRequest actual = adoptionService.createAdoptionRequest(testRequest, testUser);
		
		assertEquals(expected, actual);
	}
	
	@ParameterizedTest
	@MethodSource("invalidCreateRequests")
	void testCreateAdoptionRequest_negative(int id, String description) {
		Assertions.assertThrows(BadInputException.class, () -> {
			User testUser = new User();
			CreateRequestDTO testRequest = new CreateRequestDTO(id, description);
			adoptionService.createAdoptionRequest(testRequest, testUser);
		});
	}
	
	private static Stream<Arguments> invalidCreateRequests(){
		return Stream.of(
				Arguments.of(0,"description"),
				Arguments.of(1," "),
				Arguments.of(1,null)
				);
	}

	@Test
	void testGetAllAdoptionRequests_positive_customer() throws NotFoundException, NotAuthorizedException {
		List<AdoptionRequest> expected = new ArrayList<>();
		User testUser = new User();
		testUser.setUser_role(new UserRole(1, "customer"));
		
		when(adoptionRepo.getUserAllAdoptionRequest(testUser)).thenReturn(expected);
		
		List<AdoptionRequest> actual = adoptionService.getAllAdoptionRequests(testUser);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetAllAdoptionRequests_positive_manager() throws NotFoundException, NotAuthorizedException {
		List<AdoptionRequest> expected = new ArrayList<>();
		User testUser = new User();
		testUser.setUser_role(new UserRole(1, "manager"));
		
		when(adoptionRepo.getManagerAllAdoptionRequest()).thenReturn(expected);
		
		List<AdoptionRequest> actual = adoptionService.getAllAdoptionRequests(testUser);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetAllAdoptionRequests_negative() {
		Assertions.assertThrows(NotAuthorizedException.class, () -> {
			User testUser = new User();
			testUser.setUser_role(new UserRole(1, "not customer"));
			adoptionService.getAllAdoptionRequests(testUser);
		});
	}
	
	@Test
	void testGetRequestById_positive_customer() throws NotFoundException, NotAuthorizedException, BadInputException {
		AdoptionRequest expected = new AdoptionRequest();
		User testUser = new User();
		testUser.setUser_role(new UserRole(1, "customer"));
		
		when(adoptionRepo.getUserAdoptionRequestById(1, testUser)).thenReturn(expected);
		
		AdoptionRequest actual = adoptionService.getRequestById(1, testUser);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetRequestById_positive_manager() throws NotFoundException, NotAuthorizedException, BadInputException {
		AdoptionRequest expected = new AdoptionRequest();
		User testUser = new User();
		testUser.setUser_role(new UserRole(1, "manager"));
		
		when(adoptionRepo.getManagerAdoptionRequestById(1)).thenReturn(expected);
		
		AdoptionRequest actual = adoptionService.getRequestById(1, testUser);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetRequestById_negative_id() {
		Assertions.assertThrows(BadInputException.class, () -> {
			User testUser = new User();
			testUser.setUser_role(new UserRole(1, "customer"));
			adoptionService.getRequestById(-1, testUser);
		});
	}
	
	@Test
	void testGetRequestById_negative_user() {
		Assertions.assertThrows(NotAuthorizedException.class, () -> {
			User testUser = new User();
			testUser.setUser_role(new UserRole(1, "not customer"));
			adoptionService.getRequestById(1, testUser);
		});
	}
	
	@Test
	void testApproveDenyRequest_positive() throws UpdateException, NotFoundException, NotAuthorizedException {
		AdoptionRequest expected = new AdoptionRequest();
		User testUser = new User();
		testUser.setUser_role(new UserRole(1, "manager"));
		UpdateAdoptionRequestDTO testUpdate = new UpdateAdoptionRequestDTO();
		
		when(adoptionRepo.managerApproveDenyRequest(1, testUpdate)).thenReturn(expected);
		
		AdoptionRequest actual = adoptionService.approveDenyRequest(1,testUpdate, testUser);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testApproveDenyRequest_negative() {
		Assertions.assertThrows(NotAuthorizedException.class, () -> {
			User testUser = new User();
			testUser.setUser_role(new UserRole(1, "not customer"));
			adoptionService.getAllAdoptionRequests(testUser);
		});
	}
}
