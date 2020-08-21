package br.com.cauezito.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cauezito.api.model.Telephone;

public interface TelephoneRepository extends JpaRepository<Telephone, Long> {

}
