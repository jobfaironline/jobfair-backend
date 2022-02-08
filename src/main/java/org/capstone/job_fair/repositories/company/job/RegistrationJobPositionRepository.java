package org.capstone.job_fair.repositories.company.job;

import org.capstone.job_fair.models.entities.company.job.RegistrationJobPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RepositoryRestResource(path = "registration-job-positions")
public interface RegistrationJobPositionRepository extends JpaRepository<RegistrationJobPositionEntity, String> {
}
