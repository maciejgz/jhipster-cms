package pl.mg.jhipster.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mg.jhipster.cms.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
