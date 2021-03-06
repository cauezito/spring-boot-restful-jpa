package br.com.cauezito.api.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import br.com.cauezito.api.model.Person;

@Repository
@Component
public interface PersonRepository extends JpaRepository<Person, Long> {
	
	@Query("select p from Person p where p.login = ?1")
	Person findPersonByLogin(String login);
	
	Optional<Person> findById(Long id);

	List<Person> findByName(String name);
	
	@Transactional
	@Modifying
	@Query(value = "update person set pass = ?1 where id = ?2", nativeQuery = true)
	void updatePassword(String password, Long personId);

	default Page<Person> findPersonByNamePage(String name, PageRequest pageRequest){
		Person person = new Person();
		person.setName(name);
		ExampleMatcher em = ExampleMatcher.matchingAny()
				.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		Example<Person> example = Example.of(person, em);
		Page<Person> personReturn = findAll(example, pageRequest);
		return personReturn;
	}
	
	
	
	
}
