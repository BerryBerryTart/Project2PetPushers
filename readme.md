# Pets For Lonely People

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=BerryBerryTart_Project2PetPushers&metric=coverage)](https://sonarcloud.io/dashboard?id=BerryBerryTart_Project2PetPushers)

### Written By:

- Chakong Xiong
- Biren Sarvaiya
- Zuberi Reid

## Project Description
Pets For Lonely People is a pet adoption website. Visitors can freely browse pets that are looking for new homes. In order for users to adopt a pet they must first register an account and submit an adoption request. Adoption requests are then reviewed by managers which are either approved or denied. Users can then view the status of their adoption requests within a seperate view. For those who either cannot house a real pet or just aren't ready, they can adopt a digital pet. Users will still make a request for a digital pet but they're automatically approved.

## Technology Used
- Java 1.8
- Angular
- Spring
- JDBC
- Logback / SLF4J
- JUnit 5
- Mockito
- MariaDB / H2 (testing)
- Hibernate
- JaCoCo
- Maven

### Frontend Repoitory
[Github Repository](https://github.com/bsarvaiya24/project2angular)

# Backend Endpoints

## User Login
- `POST /register_account` : Allows user to register an account
- `POST /login_account` : Allows user to login with valid credentials
    - Will display an error if login credentials are incorrect
- `GET /logout_account` : destroys associated account session and logs user out

#### Related Fields
- `users_id`
    - Generated on creation
    - Uniquely identifies a user
- `username`
    - **Required on login**
    - Required to **register**
    - Maximum length `50`
    - Unique
- `password`
    - **Required on login**
    - Required to **register**
    - Securely hashed with SHA-256
- `first_name`
    - Only required to **register**
    - Maximum length `100`
    - No numbers
- `last_name`
    - Only required to **register**
    - Maximum length `100`
    - No numbers
- `email`
    - Only required to **register**
    - Maximum length `150`
    - Unique
- `role_id`
    - Defaults to `customer` on creation
    - Used to related a customer to their permissive role
    - Of type `customer` or `manager`

## Pet Management
- `GET /view_adoptable_pet` : Returns a list of pets that are unadopted. adopted pets will not show up
- `GET /view_adoptable_pet/:id` : Shows the profile of an individual pet
    - Customers can make an adoption request for a pet here if it's still available
- `POST /create_pet_adoption` : Submits a new pet for adoption
    - **Managers Only**
- `PUT /update_pet_adoption/:id` : Updates an existing pet adoption profile
    - **Managers Only**
- `DELETE /delete_pet_adoption/:id` : Deletes an existing pet adoption profile
    - **Managers Only**

#### Related Fields
- `pet_id`
    - Generated on creation
    - Uniquely identifies a pet
- `pet_name`
    - Maximum length `50`
    - The pet's displayed name
    - Can have letters and numbers
- `pet_age`
    - The pet's age
- `pet_species`
    - Maximum length `50`
    - The pet's respective species i.e. cat, dog
- `pet_breed`
    - Maximum length `50`
    - The pet's respective breed i.e. siamese cat, munchkin cat
- `pet_description`
    - Maximum length `255`
    - A short description of the pet
- `pet_list_date`
    - Timestamp generated when a pet is listed
- `pet_image`
    - Maximum size `16Mb`
    - An image of the pet
- `pet_status`
    - Byte array image of the respective pet
    - Defaults to `unadopted`
    - Of type `unadopted` or `adopted`
- `pet_type`
    - Defines a pet as real or digital
    - Of type `real` or `digital`

## Adoption Requests
- `GET /view_adoption_status` : Returns all adoption requests
    - Customers can only view their adoption requests
    - Managers can view all adoption requests
- `GET /view_adoption_status/:id` : Returns a specific adoption request if it exists
    - Customers can only view their adoption requests
    - Managers can view all adoption requests
- `POST /make_adoption_request` : Submits an adoption request for a certain pet
    - Will throw an error if there already is an existing request made by a customer for the respective pet
- `PUT update_adoption_request/:id` : Approves / Denies an existing request
    - **Managers Only**
    - Requests approved or denied cannot be altered
    - If a request is approved for a pet, all other existing requests made by other customers will be denied

#### Related Fields
- `adoption_request_id`
    - Generated on creation
    - Uniquely identifies an adoption request
- `adoption_request_user`
    - Relates an adoption request to the customer who made it
- `adoption_request_pet`
    - Relates an adoption request to the pet being requested for adoption
- `adoption_request_status`
    - Approval status of the adoption request
    - Upon creation, defaults to `pending`
    - Of type `pending`, `approved`, or `rejected`
- `adoption_request_description`
    - Maximum length `255`
    - Customer submitted description on why they want to adopt a pet
- `adoption_request_response`
    - Maximum length `255`
    - Manager submitted response on why the adoption request was either approved or denied
- `adoption_request_created`
    - Timestamp generated when an adoption request is submitted
- `adoption_request_resolved`
    - Timestamp generated when an adoption request is approved / denied
