package br.com.cauezito.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.cauezito.api.model.Person;
import br.com.cauezito.api.repository.PersonRepository;

@Service
public class ImplUserDetailsService implements UserDetailsService{
	
	@Autowired
	private PersonRepository personRepository;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Person person = personRepository.findPersonByLogin(login);
		if(person == null) {
			throw new UsernameNotFoundException("Login não encontrado!");
		}		
		return new User(person.getLogin(), person.getPassword(), person.getAuthorities());
	}

}
