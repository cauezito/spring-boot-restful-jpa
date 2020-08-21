package br.com.cauezito.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cauezito.api.model.Occupation;
import br.com.cauezito.api.repository.OccupationRepository;

@RestController
@RequestMapping(value = "/occupation")
public class OccupationController {
	
	@Autowired
	private OccupationRepository occupationRepository;
	
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Occupation>> occupations(){
		List<Occupation> list = occupationRepository.findAll();
		return new ResponseEntity<List<Occupation>>(list, HttpStatus.OK);
	}
}
