package ma.rest.spring.repositories;

import ma.rest.spring.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

/** Opérations de persistance déléguées à Spring Data JPA. */
@org.springframework.stereotype.Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {}
