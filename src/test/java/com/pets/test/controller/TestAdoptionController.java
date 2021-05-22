package com.pets.test.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pets.DTO.CreateRequestDTO;
import com.pets.DTO.UpdateAdoptionRequestDTO;
import com.pets.controller.AdoptionController;
import com.pets.exception.BadInputException;
import com.pets.exception.CreationException;
import com.pets.exception.NotAuthorizedException;
import com.pets.exception.NotFoundException;
import com.pets.exception.UpdateException;
import com.pets.model.AdoptionRequest;
import com.pets.model.User;
import com.pets.service.AdoptionService;

@ExtendWith(MockitoExtension.class)
class TestAdoptionController {
	@Autowired
	MockMvc mockmvc;
	@Mock
	AdoptionService adoptionService;
	@Mock
	HttpServletRequest request;
	@Mock
	HttpSession session;
	@Mock
	User user;
	@InjectMocks
	AdoptionController adoptionController;

	private ObjectMapper om = new ObjectMapper();

	@BeforeEach
	void setUp() throws Exception {
		this.mockmvc = MockMvcBuilders.standaloneSetup(adoptionController).build();
	}

	@Test
	void testCreatAdoptionRequest_positive() throws Exception {
		AdoptionRequest expected = new AdoptionRequest();
		String expectedJson = om.writeValueAsString(expected);
		CreateRequestDTO test = new CreateRequestDTO();
		String testJson = om.writeValueAsString(test);

		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("loggedInUser")).thenReturn(user);
		when(adoptionService.createAdoptionRequest(test, user)).thenReturn(expected);

		this.mockmvc.perform(post("/make_adoption_request")
				.contentType(MediaType.APPLICATION_JSON).content(testJson)).andExpect(status().isCreated())
				.andExpect(content().json(expectedJson));
	}

	@Test
	void testCreateAdoptionRequest_negative_notFound() throws Exception {
		CreateRequestDTO test = new CreateRequestDTO();
		String testJson = om.writeValueAsString(test);

		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("loggedInUser")).thenReturn(user);
		when(adoptionService.createAdoptionRequest(test, user)).thenThrow(NotFoundException.class);

		this.mockmvc.perform(post("/make_adoption_request")
				.contentType(MediaType.APPLICATION_JSON).content(testJson)).andExpect(status().isNotFound());
	}

	@Test
	void testCreateAdoptionRequest_negative_badInput() throws Exception {
		CreateRequestDTO test = new CreateRequestDTO();
		String testJson = om.writeValueAsString(test);

		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("loggedInUser")).thenReturn(user);
		when(adoptionService.createAdoptionRequest(test, user)).thenThrow(BadInputException.class);

		this.mockmvc.perform(post("/make_adoption_request")
				.contentType(MediaType.APPLICATION_JSON).content(testJson)).andExpect(status().isBadRequest());
	}

	@Test
	void testCreateAdoptionRequest_negative_creation() throws Exception {
		CreateRequestDTO test = new CreateRequestDTO();
		String testJson = om.writeValueAsString(test);

		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("loggedInUser")).thenReturn(user);
		when(adoptionService.createAdoptionRequest(test, user)).thenThrow(CreationException.class);

		this.mockmvc.perform(post("/make_adoption_request")
				.contentType(MediaType.APPLICATION_JSON).content(testJson)).andExpect(status().isInternalServerError());
	}

	@Test
	void testGetAllRequest_positive() throws Exception {
		List<AdoptionRequest> expected = new ArrayList<>();
		String expectedJson = om.writeValueAsString(expected);

		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("loggedInUser")).thenReturn(user);
		when(adoptionService.getAllAdoptionRequests(user)).thenReturn(expected);

		this.mockmvc.perform(get("/view_adoption_status")).andExpect(status().isOk())
				.andExpect(content().json(expectedJson));
	}

	@Test
	void testGetAllRequest_negative_notFound() throws Exception {
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("loggedInUser")).thenReturn(user);
		when(adoptionService.getAllAdoptionRequests(user)).thenThrow(NotFoundException.class);

		this.mockmvc.perform(get("/view_adoption_status")).andExpect(status().isNotFound());
	}

	@Test
	void testGetAllRequest_negative_notAuthorized() throws Exception {
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("loggedInUser")).thenReturn(user);
		when(adoptionService.getAllAdoptionRequests(user)).thenThrow(NotAuthorizedException.class);

		this.mockmvc.perform(get("/view_adoption_status")).andExpect(status().isUnauthorized());
	}

	@Test
	void testGetById_positive() throws Exception {
		AdoptionRequest expected = new AdoptionRequest();
		String expectedJson = om.writeValueAsString(expected);

		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("loggedInUser")).thenReturn(user);
		when(adoptionService.getRequestById(eq(1), eq(user))).thenReturn(expected);

		this.mockmvc.perform(get("/view_adoption_status/1")).andExpect(status().isOk())
				.andExpect(content().json(expectedJson));
	}

	@Test
	void testGetById_negative_notFound() throws Exception {
		when(request.getSession(eq(false))).thenReturn(session);
		when(session.getAttribute(eq("loggedInUser"))).thenReturn(user);
		when(adoptionService.getRequestById(eq(1), eq(user))).thenThrow(NotFoundException.class);

		this.mockmvc.perform(get("/view_adoption_status/1")).andExpect(status().isNotFound());
	}

	@Test
	void testGetById_negative_notAuthorized() throws Exception {
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("loggedInUser")).thenReturn(user);
		when(adoptionService.getRequestById(eq(1), eq(user))).thenThrow(NotAuthorizedException.class);

		this.mockmvc.perform(get("/view_adoption_status/1"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void testUpdateRequest_positive() throws Exception {
		AdoptionRequest expected = new AdoptionRequest();
		String expectedJson = om.writeValueAsString(expected);
		UpdateAdoptionRequestDTO testRequest = new UpdateAdoptionRequestDTO();
		String testJson = om.writeValueAsString(testRequest);

		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("loggedInUser")).thenReturn(user);
		when(adoptionService.approveDenyRequest(eq(1), eq(testRequest), eq(user))).thenReturn(expected);

		this.mockmvc
				.perform(put("/update_adoption_request/1").contentType(MediaType.APPLICATION_JSON).content(testJson))
				.andExpect(status().isOk()).andExpect(content().json(expectedJson));
	}

	@Test
	void testUpdateRequest_negative_notAuthorized() throws Exception {
		UpdateAdoptionRequestDTO testRequest = new UpdateAdoptionRequestDTO();
		String testJson = om.writeValueAsString(testRequest);

		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("loggedInUser")).thenReturn(user);
		when(adoptionService.approveDenyRequest(eq(1), eq(testRequest), eq(user))).thenThrow(NotAuthorizedException.class);

		this.mockmvc
				.perform(put("/update_adoption_request/1").contentType(MediaType.APPLICATION_JSON).content(testJson))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void testUpdateRequest_negative_update() throws Exception {
		UpdateAdoptionRequestDTO testRequest = new UpdateAdoptionRequestDTO();
		String testJson = om.writeValueAsString(testRequest);

		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("loggedInUser")).thenReturn(user);
		when(adoptionService.approveDenyRequest(eq(1), eq(testRequest), eq(user))).thenThrow(UpdateException.class);

		this.mockmvc
				.perform(put("/update_adoption_request/1").contentType(MediaType.APPLICATION_JSON).content(testJson))
				.andExpect(status().isInternalServerError());
	}

	@Test
	void testUpdateRequest_negative_notFound() throws Exception {
		UpdateAdoptionRequestDTO testRequest = new UpdateAdoptionRequestDTO();
		String testJson = om.writeValueAsString(testRequest);

		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("loggedInUser")).thenReturn(user);
		when(adoptionService.approveDenyRequest(eq(1), eq(testRequest), eq(user))).thenThrow(NotFoundException.class);

		this.mockmvc
				.perform(put("/update_adoption_request/1").contentType(MediaType.APPLICATION_JSON).content(testJson))
				.andExpect(status().isNotFound());
	}
}
