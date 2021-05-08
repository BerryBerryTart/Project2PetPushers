DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_roles;

DROP TABLE IF EXISTS adoption_requests;
DROP TABLE IF EXISTS adoption_requests_status;

DROP TABLE IF EXISTS pets;
DROP TABLE IF EXISTS pet_status;
DROP TABLE IF EXISTS pet_type;

CREATE TABLE pet_status (
	pet_status_id INTEGER(20) PRIMARY KEY AUTO_INCREMENT,
	pet_status VARCHAR(20) NOT NULL
);

CREATE TABLE pet_type (
	pet_type_id INTEGER(20) PRIMARY KEY AUTO_INCREMENT,
	pet_type VARCHAR(20) NOT NULL
);

CREATE TABLE pets (
	pet_id INTEGER PRIMARY KEY AUTO_INCREMENT,
	pet_name VARCHAR(20) NOT NULL,
	pet_age INTEGER(10) NOT NULL,
	pet_species VARCHAR(20) NOT NULL,
	pet_breed VARCHAR(20),
	pet_description VARCHAR(250),
	pet_image BLOB,
	pet_status_id INTEGER(20) NOT NULL,
	pet_type_id INTEGER(20) NOT NULL,
	CONSTRAINT `fk_pets_pet_status` FOREIGN KEY (pet_status_id) REFERENCES pet_status (pet_status_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_pets_pet_type` FOREIGN KEY (pet_type_id) REFERENCES pet_type (pet_type_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE user_roles (
	user_role_id INTEGER(20) PRIMARY KEY AUTO_INCREMENT,
	user_role VARCHAR(20) NOT NULL
);

CREATE TABLE users (
	user_id INTEGER(20) PRIMARY KEY AUTO_INCREMENT,
	user_username VARCHAR(50) NOT NULL,
	user_password VARCHAR(50) NOT NULL,
	user_first_name VARCHAR(100) NOT NULL,
	user_last_name VARCHAR(100) NOT NULL,
	user_email VARCHAR(150) NOT NULL,
	user_role_id INTEGER(20) NOT NULL,
	CONSTRAINT `un_users` UNIQUE (user_username,user_email),
	CONSTRAINT `fk_users_user_roles` FOREIGN KEY (user_role_id) REFERENCES user_roles (user_role_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE adoption_requests_status (
	adoption_request_status_id INTEGER(20) PRIMARY KEY AUTO_INCREMENT,
	adoption_request_status VARCHAR(20) NOT NULL
);

CREATE TABLE adoption_requests (
	adoption_request_id INTEGER PRIMARY KEY AUTO_INCREMENT,
	adoption_request_pet_id INTEGER NOT NULL,
	adoption_request_user_id INTEGER NOT NULL,
	adoption_request_status_id INTEGER NOT NULL,
	adoption_request_description VARCHAR(250),
	adoption_request_response VARCHAR(250),
	CONSTRAINT `fk_adoption_requests_pets` FOREIGN KEY (adoption_request_pet_id) REFERENCES pets (pet_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_adoption_requests_users` FOREIGN KEY (adoption_request_user_id) REFERENCES users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_adoption_requests_adoption_requests_status` FOREIGN KEY (adoption_request_status_id) REFERENCES adoption_requests_status (adoption_request_status_id) ON DELETE CASCADE ON UPDATE CASCADE
);