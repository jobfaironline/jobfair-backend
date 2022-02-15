package org.capstone.job_fair.repositories.company.job;

import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPositionRepository extends JpaRepository<JobPositionEntity, String> {
}