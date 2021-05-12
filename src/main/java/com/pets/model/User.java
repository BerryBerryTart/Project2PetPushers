package com.pets.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int user_id;

	@Column(name = "username", length = 50, unique = true)
	private String username;

	@JsonIgnore
	@Column(name = "password", length = 65)
	private String password;

	@Column(name = "first_name", length = 100)
	private String first_name;

	@Column(name = "last_name", length = 100)
	private String last_name;

	@Column(name = "email", length = 150, unique = true)
	private String email;

	// TODO: keep an eye on this one
	@OneToOne(cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "user_role_id", referencedColumnName = "user_role_id")
	private UserRole user_role;

	public User() {
	}

	public User(String username, String password, String first_name, String last_name, String email) {
		super();
		this.username = username;
		this.password = password;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
	}
}
