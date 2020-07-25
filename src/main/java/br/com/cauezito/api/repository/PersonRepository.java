package br.com.cauezito.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.cauezito.api.model.Person;

@Repository

public interface PersonRepository extends CrudRepository<Person, Long> {

	
}
