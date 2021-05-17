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

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
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

}
