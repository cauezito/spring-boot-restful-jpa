package br.com.cauezito.api.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
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
import br.com.cauezito.api.model.PersonDTO;
import br.com.cauezito.api.repository.PersonRepository;
import br.com.cauezito.api.repository.TelephoneRepository;
import br.com.cauezito.api.service.ImplUserDetailsService;
import br.com.cauezito.api.service.ReportService;

@CrossOrigin
@RestController
@RequestMapping(value = "/user")

public class IndexController {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private TelephoneRepository telephoneRepository;

	@Autowired
	private ImplUserDetailsService userDetails;
	
	@Autowired
	private ReportService reportService;

	// GET

	@GetMapping(value = "/{id}", produces = "application/json")
	@CachePut("cacheuser")
	public ResponseEntity<Person> find(@PathVariable(value = "id") Long id) {
		Person user = personRepository.findById(id).get();

		return new ResponseEntity<Person>(user, HttpStatus.OK);
	}

	@GetMapping(value = "/find/{name}", produces = "application/json")
	public ResponseEntity<Page<Person>> findByName(@PathVariable(value = "name") String name) {
		Page<Person> list = null;		
		PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("name"));
		if (name == null || name.equalsIgnoreCase("undefined") || (name != null && name.trim().isEmpty())) {
			list = personRepository.findAll(pageRequest);
		} else {
			list = personRepository.findPersonByNamePage(name, pageRequest);
		}
		return new ResponseEntity<Page<Person>>(list, HttpStatus.OK);
	}

		@GetMapping(value = "/find/{name}/page/{page}", produces = "application/json")
		public ResponseEntity<Page<Person>> findByNamePage(@PathVariable(value = "name") String name,
				@PathVariable(value = "page") int page) {
			Page<Person> list = null;		
			PageRequest pageRequest = PageRequest.of(page, 5, Sort.by("name"));
			if (name == null || name.equalsIgnoreCase("undefined") || (name != null && name.trim().isEmpty())) {
				list = personRepository.findAll(pageRequest);
			} else {
				list = personRepository.findPersonByNamePage(name, pageRequest);
			}
		
		
		return new ResponseEntity<Page<Person>>(list, HttpStatus.OK);
	}
		
		@GetMapping(value = "/report", produces = "application/text")
		public ResponseEntity<String> downloadReport(HttpServletRequest req){
			byte[] pdf = reportService.buildReport("users", req.getServletContext());
			
			String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);
			
			return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);
			
		}

	@CachePut(value = "people")
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<Page<Person>> allUsers() {
		PageRequest page = PageRequest.of(0, 5, Sort.by("name"));
		Page<Person> list = personRepository.findAll(page);

		return new ResponseEntity<Page<Person>>(list, HttpStatus.OK);
	}

	@CachePut(value = "people")
	@GetMapping(value = "/page/{page}", produces = "application/json")
	public ResponseEntity<Page<Person>> allUsersPage(@PathVariable("page") int pageParam) {
		PageRequest page = PageRequest.of(pageParam, 5, Sort.by("name"));
		Page<Person> list = personRepository.findAll(page);

		return new ResponseEntity<Page<Person>>(list, HttpStatus.OK);
	}

	// POST

	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Person> register(@RequestBody Person person) {
		person.getPhones().forEach(t -> t.setPerson(person));
		String password = new BCryptPasswordEncoder().encode(person.getPass());
		person.setPass(password);
		Person userAux = personRepository.save(person);
		return new ResponseEntity<Person>(userAux, HttpStatus.OK);
	}

	// PUT

	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Person> update(@RequestBody Person person) {
		person.getPhones().forEach(t -> t.setPerson(person));
		Person aux = personRepository.findById(person.getId()).get();

		if (!aux.getPass().equals(person.getPass())) {
			String password = new BCryptPasswordEncoder().encode(person.getPass());
			person.setPass(password);
		}

		Person userAux = personRepository.save(person);
		return new ResponseEntity<Person>(userAux, HttpStatus.OK);
	}

	// DELETE

	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String delete(@PathVariable(value = "id") Long id) {
		personRepository.deleteById(id);
		return "ok";
	}

	@DeleteMapping(value = "/deleteTelephone/{id}", produces = "application/text")
	public String deleteTelephone(@PathVariable("id") Long id) {
		telephoneRepository.deleteById(id);
		return "ok";
	}
}
