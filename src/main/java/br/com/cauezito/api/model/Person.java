package br.com.cauezito.api.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ForeignKey;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Person implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true)
	private String login;
	private String pass;
	private String name;
	private String surname;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", uniqueConstraints = @UniqueConstraint (
			columnNames = {"person_id", "role_id"}, name = "unique_role"
			), joinColumns = @JoinColumn(name = "person_id", referencedColumnName = "id", 
			table = "person", unique = false,  foreignKey = @javax.persistence.ForeignKey(name="person_fk", 
			value = ConstraintMode.CONSTRAINT)), 	inverseJoinColumns = @JoinColumn(name="role_id",
			referencedColumnName = "id", table = "role", unique=false, updatable = false, foreignKey =
			@javax.persistence.ForeignKey(name="role_fk", 	value = ConstraintMode.CONSTRAINT)))
	
	private List<Role> roles;
	
	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Telephone> phones = new ArrayList<Telephone>();
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPass() {
		return this.pass;
	}
	public void setPass(String password) {
		this.pass = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public List<Telephone> getPhones() {
		return phones;
	}
	public void setPhones(List<Telephone> phones) {
		this.phones = phones;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}
	@Override
	public String getUsername() {
		return this.login;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}
	@JsonIgnore
	@Override
	public String getPassword() {
		return this.pass;
	}
	
}
