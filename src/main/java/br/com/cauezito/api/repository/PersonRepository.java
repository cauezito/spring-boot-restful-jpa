package br.com.cauezito.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.cauezito.api.model.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
	
	@Query("select p from Person p where p.login = ?1")
	Person findPersonByLogin(String login);
	@Query("select p from Person p where p.name = ?1")
	List<Person> findPersonByName(String name);
	
}
