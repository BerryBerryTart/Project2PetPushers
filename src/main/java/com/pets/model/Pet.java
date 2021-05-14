package com.pets.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pets")
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode
public class Pet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pet_id")
	private int pet_id;

	@Column(name = "pet_name", length = 50)
	private String pet_name;

	@Column(name = "pet_age")
	private int pet_age;

	@Column(name = "pet_species", length = 50)
	private String pet_species;

	@Column(name = "pet_breed", length = 50)
	private String pet_breed;

	@Column(name = "pet_description", length = 255)
	private String pet_description;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "pet_list_date")
	private Date pet_list_date;

	@Column(name = "pet_image", columnDefinition = "MEDIUMBLOB")
	private byte[] pet_image;
	
	@OneToOne
	@JoinColumn(name = "pet_type", referencedColumnName = "pet_type_id")
	private PetType pet_type;
	
	@OneToOne
	@JoinColumn(name = "pet_status", referencedColumnName = "pet_status_id")
	private PetStatus pet_status;
}
