package org.capstone.job_fair.repositories.job_fair.job_fair_booth;

import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothLayoutVideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobFairBoothLayoutVideoRepository extends JpaRepository<JobFairBoothLayoutVideoEntity, String> {
}
