package com.pets.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity
@Table(name = "adoption_request_status")
@Proxy(lazy = false)
public class AdoptionRequestStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "adoption_request_id")
	private int adoption_request_id;

	@Column(name = "adoption_request", length = 15, unique = true)
	private String adoption_request;

	public AdoptionRequestStatus() {
	}
}
