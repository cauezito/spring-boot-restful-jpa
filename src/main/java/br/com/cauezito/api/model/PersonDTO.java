package br.com.cauezito.api.model;

import java.io.Serializable;

public class PersonDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nickname;
	private String user;
	private String lastName;
	
	public PersonDTO(Person person) {
		this.nickname = person.getLogin();
		this.user = person.getName();
		this.lastName = person.getSurname();
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
