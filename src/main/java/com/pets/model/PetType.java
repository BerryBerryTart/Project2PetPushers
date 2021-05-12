package com.pets.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity
@Table(name = "pet_type")
@Proxy(lazy = false)
public class PetType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pet_type_id")
	private int pet_type_id;

	@Column(name = "pet_type", length = 15, unique = true)
	private String pet_type;

	public PetType() {
	}
}
