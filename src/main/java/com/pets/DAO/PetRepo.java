package com.pets.DAO;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pets.DTO.PetDTO;
import com.pets.exception.NotFoundException;
import com.pets.exception.UpdateException;
import com.pets.model.Pet;
import com.pets.model.PetStatus;
import com.pets.model.PetType;

@Repository
public class PetRepo {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public Pet createPet(PetDTO createPetDTO) {
		Session session = sessionFactory.getCurrentSession();
		Pet pet = new Pet();		
		
		pet.setPet_age(createPetDTO.getPet_age());
		pet.setPet_name(createPetDTO.getPet_name());
		pet.setPet_breed(createPetDTO.getPet_breed());
		pet.setPet_species(createPetDTO.getPet_species());
		pet.setPet_description(createPetDTO.getPet_description());
		
		//image is optional
		if (createPetDTO.getPet_image() != null && createPetDTO.getPet_image().length > 0) {
			pet.setPet_image(createPetDTO.getPet_image());
		}
		
		//status setup stuff
		
		/* Status where:
		 * 1. unadopted
		 * 2. adopted
		 * */
		String hqlStatus = "FROM PetStatus ps WHERE ps.pet_status=:pet_status";
		Query<?> statusQuery = session.createQuery(hqlStatus);
		statusQuery.setParameter("pet_status", "unadopted");				
		PetStatus ps = (PetStatus) statusQuery.getSingleResult();		
		PetStatus status = session.load(PetStatus.class, ps.getPet_status_id());
		pet.setPet_status(status);
		
		/* Type where:
		 * 1. real
		 * 2. digital
		 * */
		String hqlType = "FROM PetType pt WHERE pt.pet_type=:pet_type";
		Query<?> typeQuery = session.createQuery(hqlType);
		typeQuery.setParameter("pet_type", createPetDTO.getPet_type());
		PetType pt = (PetType) typeQuery.getSingleResult();	
		PetType petType = session.load(PetType.class, pt.getPet_type_id());
		pet.setPet_type(petType);
		
		session.persist(pet);
		
		return pet;
	}
	
	//returns a list of REAL pets that are unadopted
	@Transactional
	public List<Pet> getAllPets() throws NotFoundException{
		Session session = sessionFactory.getCurrentSession();
		List<Pet> petList = null;
		
		//load up proxy object
		String hqlStatus = "FROM PetStatus ps WHERE ps.pet_status=:pet_status";
		Query<?> statusQuery = session.createQuery(hqlStatus);
		statusQuery.setParameter("pet_status", "unadopted");				
		PetStatus ps = (PetStatus) statusQuery.getSingleResult();		
		PetStatus status = session.load(PetStatus.class, ps.getPet_status_id());
		
		//get the list
		String hql = "FROM Pet p WHERE p.pet_status=:status";
		
		Query<Pet> listQuery = session.createQuery(hql);
		listQuery.setParameter("status", status);	
		petList = listQuery.getResultList();
		
		
		return petList;
	}
	
	@Transactional
	public Pet getPetById(int id) throws NotFoundException {
		Session session = sessionFactory.getCurrentSession();
		Pet pet = null;
		
		String hql = "FROM Pet p WHERE p.pet_id = :id";
		
		try {
			Query<?> query = session.createQuery(hql);
			query.setParameter("id", id);
			
			pet = (Pet) query.getSingleResult();
		} catch (NoResultException e) {
			throw new NotFoundException("pet with id " + id + " not found");
		}
		
		return pet;
	}
	
	public Pet updatePetById(int id, PetDTO updatePetDTO) throws UpdateException {
		Session session = sessionFactory.getCurrentSession();
		Pet pet = null;
		
		String hql = "UPDATE Pet p SET p.pet_name=:name, p.pet_age=:age, p.pet_species=:species, p.pet_breed=:breed,"
				+ "p.pet_description=:description ";		
		
		if (updatePetDTO.getPet_image() != null && updatePetDTO.getPet_image().length > 0) {
			hql = hql.concat(", p.pet_image=:image ");
		}
		
		//lastly add where condition
		hql = hql.concat(" WHERE p.pet_id = :id");
		
		Query<?> query = session.createQuery(hql);
		query.setParameter("name", updatePetDTO.getPet_name());
		query.setParameter("age", updatePetDTO.getPet_age());
		query.setParameter("species", updatePetDTO.getPet_species());
		query.setParameter("breed", updatePetDTO.getPet_breed());
		query.setParameter("description", updatePetDTO.getPet_description());
		
		//updating pet image from params is optional
		if (updatePetDTO.getPet_image() != null && updatePetDTO.getPet_image().length > 0) {
			query.setParameter("image", updatePetDTO.getPet_image());
		}
		
		query.setParameter("id", id);
		
		int updateCount = query.executeUpdate();
		
		if (updateCount <= 0) {
			throw new UpdateException("Failed to update pet with id " + id);
		}
		
		//get the newly updated pet
		pet = session.get(Pet.class, id);
		
		return pet;
	}
	
	public boolean deletePetById(int id) throws UpdateException {
		Session session = sessionFactory.getCurrentSession();
		
		String hql = "DELETE FROM Pet p WHERE p.pet_id = :id";
		
		Query<?> query = session.createQuery(hql);
		query.setParameter("id", id);
		
		int updateCount = query.executeUpdate();
		
		if (updateCount <= 0) {
			throw new UpdateException("Failed to delete pet with id " + id);
		}
		
		return true;
	}
}
