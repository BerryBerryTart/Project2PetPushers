DROP TABLE IF EXISTS adoption_requests;
DROP TABLE IF EXISTS adoption_request_status;

DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS user_role;

DROP TABLE IF EXISTS pets;
DROP TABLE IF EXISTS pet_status;
DROP TABLE IF EXISTS pet_type;

CREATE TABLE pet_status (
	pet_status_id INTEGER(20) PRIMARY KEY AUTO_INCREMENT,
	pet_status VARCHAR(15) NOT NULL UNIQUE
);

CREATE TABLE pet_type (
	pet_type_id INTEGER(20) PRIMARY KEY AUTO_INCREMENT,
	pet_type VARCHAR(15) NOT NULL UNIQUE
);

CREATE TABLE pets (
	pet_id INTEGER PRIMARY KEY AUTO_INCREMENT,
	pet_name VARCHAR(50) NOT NULL,
	pet_age INTEGER(10) NOT NULL,
	pet_species VARCHAR(50) NOT NULL,
	pet_breed VARCHAR(50),
	pet_description VARCHAR(255),
	pet_list_date TIMESTAMP,
	pet_image MEDIUMBLOB,
	pet_status INTEGER(20) NOT NULL,
	pet_type INTEGER(20) NOT NULL,
	CONSTRAINT `fk_pets_pet_status` FOREIGN KEY (pet_status) REFERENCES pet_status (pet_status_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_pets_pet_type` FOREIGN KEY (pet_type) REFERENCES pet_type (pet_type_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE user_role (
	user_role_id INTEGER(20) PRIMARY KEY AUTO_INCREMENT,
	user_role VARCHAR(15) NOT NULL UNIQUE
);

CREATE TABLE user (
	user_id INTEGER(20) PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(50) NOT NULL,
	password VARCHAR(65) NOT NULL,
	first_name VARCHAR(100) NOT NULL,
	last_name VARCHAR(100) NOT NULL,
	email VARCHAR(150) NOT NULL,
	user_role_id INTEGER(20) NOT NULL,
	CONSTRAINT `un_users` UNIQUE (username,email),
	CONSTRAINT `fk_users_user_roles` FOREIGN KEY (user_role_id) REFERENCES user_role (user_role_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE adoption_request_status (
	adoption_request_status_id INTEGER(20) PRIMARY KEY AUTO_INCREMENT,
	adoption_request_status VARCHAR(15) NOT NULL UNIQUE
);

CREATE TABLE adoption_requests (
	adoption_request_id INTEGER PRIMARY KEY AUTO_INCREMENT,
	adoption_request_pet INTEGER(20) NOT NULL,
	adoption_request_user INTEGER(20) NOT NULL,
	adoption_request_status INTEGER(20) NOT NULL,
	adoption_request_description VARCHAR(255),
	adoption_request_response VARCHAR(255),
	adoption_request_created TIMESTAMP,
	adoption_request_resolved TIMESTAMP,
	CONSTRAINT `fk_adoption_requests_pets` FOREIGN KEY (adoption_request_pet) REFERENCES pets (pet_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_adoption_requests_users` FOREIGN KEY (adoption_request_user) REFERENCES user (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_adoption_requests_adoption_requests_status` FOREIGN KEY (adoption_request_status) REFERENCES adoption_request_status (adoption_request_status_id) ON DELETE CASCADE ON UPDATE CASCADE
);
