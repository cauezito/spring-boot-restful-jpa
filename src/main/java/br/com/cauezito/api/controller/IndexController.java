package br.com.cauezito.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.cauezito.api.model.Person;
import br.com.cauezito.api.repository.PersonRepository;

@RestController
@RequestMapping(value = "/user")
public class IndexController {

	@Autowired
	private PersonRepository personRepository;

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Person> init(@PathVariable(value = "id") Long id) {
		Optional<Person> user = personRepository.findById(id);

		return new ResponseEntity<Person>(user.get(), HttpStatus.OK);
	}

	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Person>> allUsers() {
		List<Person> users = (List<Person>) personRepository.findAll();
		return new ResponseEntity<List<Person>>(users, HttpStatus.OK);
	}

	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Person> register(@RequestBody Person user) {
		Person userAux = personRepository.save(user);
		return new ResponseEntity<Person>(userAux, HttpStatus.OK);
	}

	@PutMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Person> update(@RequestBody Person user, @PathVariable(value = "id") Long id) {
		user.setId(id);
		Person userAux = personRepository.save(user);
		return new ResponseEntity<Person>(userAux, HttpStatus.OK);

	}

	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String delete(@PathVariable(value = "id") Long id) {

		personRepository.deleteById(id);
		return "ok";
	}
}
