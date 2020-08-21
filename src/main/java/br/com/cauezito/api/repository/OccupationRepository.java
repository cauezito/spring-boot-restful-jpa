package br.com.cauezito.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cauezito.api.model.Occupation;

@Repository
public interface OccupationRepository extends JpaRepository<Occupation, Long> {

}
