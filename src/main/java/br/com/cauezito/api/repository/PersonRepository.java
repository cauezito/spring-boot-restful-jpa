package br.com.cauezito.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.cauezito.api.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
	
	@Query("select p from Person p where p.login = ?1")
	Person findPersonByLogin(String login);
	
	List<Person> findByName(String name);
	
	
	
	
	
	
	
}
