package com.pets.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "adoption_requests")
@Proxy(lazy = false)
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode
public class AdoptionRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "adoption_request_id")
	private int adoption_request_id;

	@ManyToOne
	@JoinColumn(name = "adoption_request_user", referencedColumnName = "user_id")
	private User adoption_request_user;

	@ManyToOne
	@JoinColumn(name = "adoption_request_pet", referencedColumnName = "pet_id")
	private Pet adoption_request_pet;

	@OneToOne
	@JoinColumn(name = "adoption_request_status", referencedColumnName = "adoption_request_id")
	private AdoptionRequestStatus adoption_request_status;

	@Column(name = "adoption_request_description", length = 255)
	private String adoption_request_description;

	@Column(name = "adoption_request_response", length = 255)
	private String adoption_request_response;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "adoption_request_created")
	private Date adoption_request_created;

	@Column(name = "adoption_request_resolved")
	private Date adoption_request_resolved;
}
