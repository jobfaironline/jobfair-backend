package org.capstone.job_fair.repositories.attendant;

import org.capstone.job_fair.models.entities.attendant.ResidenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@RepositoryRestResource(path = "residences")
public interface ResidenceRepository extends JpaRepository<ResidenceEntity, String> {
}
