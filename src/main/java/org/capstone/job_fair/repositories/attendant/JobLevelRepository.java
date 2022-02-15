package org.capstone.job_fair.repositories.attendant;

import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobLevelRepository extends JpaRepository<JobLevelEntity, Integer> {
}
