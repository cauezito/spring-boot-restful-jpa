package br.com.cauezito.api.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.cauezito.api.Error;
import br.com.cauezito.api.model.Person;
import br.com.cauezito.api.repository.PersonRepository;
import br.com.cauezito.api.service.SendEmailService;

@RestController
@RequestMapping(value="/recovery")
public class AccRecoveryController {
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private SendEmailService sendEmail;

	@PostMapping(value = "/")
	@ResponseBody
	public ResponseEntity<Error> recovery(@RequestBody Person login){
		Error error = new Error();
		Person person = personRepository.findPersonByLogin(login.getLogin());
		
		if(person == null) {
			error.setCode("404");
			error.setError("Not found");
		} else { //send e-mail
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String newPassword = dateFormat.format(Calendar.getInstance().getTime());
			String cryptPassword = new BCryptPasswordEncoder().encode(newPassword);
			personRepository.updatePassword(cryptPassword, person.getId());			
			sendEmail.send("Password recovery", person.getLogin(), "New password: " + newPassword);
			error.setCode("200");
			error.setError("Sent");
		}
		
		return new ResponseEntity<Error>(error, HttpStatus.OK);
	}
}
