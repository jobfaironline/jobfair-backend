package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobFairRepository extends JpaRepository<JobFairEntity, String> {
    Optional<JobFairEntity> findByIdAndCompanyId(String jobFairId, String companyId);

}
