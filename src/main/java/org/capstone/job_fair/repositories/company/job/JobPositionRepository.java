package org.capstone.job_fair.repositories.company.job;

import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RepositoryRestResource(path = "job-positions")
public interface JobPositionRepository extends JpaRepository<JobPositionEntity, String> {
    Optional<JobPositionEntity> findById(String id);
}