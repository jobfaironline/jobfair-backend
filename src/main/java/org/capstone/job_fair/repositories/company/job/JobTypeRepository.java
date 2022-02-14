package org.capstone.job_fair.repositories.company.job;

import org.capstone.job_fair.models.entities.company.job.JobTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTypeRepository extends JpaRepository<JobTypeEntity, String> {
}