package br.com.cauezito.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cauezito.api.model.Person;
import br.com.cauezito.api.repository.PersonRepository;


@CrossOrigin
@RestController
@RequestMapping(value = "/user")

public class IndexController {

	@Autowired
	private PersonRepository personRepository;

	//GET

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Person> find(@PathVariable(value = "id") Long id) {
		Optional<Person> user = personRepository.findById(id);

		return new ResponseEntity<Person>(user.get(), HttpStatus.OK);
	}
		
 	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Person>> allUsers() {
		List<Person> users = (List<Person>) personRepository.findAll();
		return new ResponseEntity<List<Person>>(users, HttpStatus.OK);
	}
	
	//POST

	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Person> register(@RequestBody Person person) {
		person.getPhones().forEach(t -> t.setPerson(person));
		String password = new BCryptPasswordEncoder().encode(person.getPass());
		person.setPass(password);
		person.getPhones().forEach(t -> t.setPerson(person));
		Person userAux = personRepository.save(person);	
		return new ResponseEntity<Person>(userAux, HttpStatus.OK);
	}

	//PUT

	@PutMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Person> update(@RequestBody Person person, @PathVariable(value = "id") Long id) {
		person.setId(id);
		person.getPhones().forEach(t -> t.setPerson(person));
		Person aux = personRepository.findPersonByLogin(person.getLogin());
		
		if(!aux.getPass().equals(person.getPass())) {
			String password = new BCryptPasswordEncoder().encode(person.getPass());
			person.setPass(password);
		}
		
		Person userAux = personRepository.save(person);
		return new ResponseEntity<Person>(userAux, HttpStatus.OK);
	}
	
	//DELETE

	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String delete(@PathVariable(value = "id") Long id) {
		personRepository.deleteById(id);
		return "ok";
	}
}
