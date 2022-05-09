package org.capstone.job_fair.repositories.job_fair.job_fair_booth;

import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothLayoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobFairBoothLayoutRepository extends JpaRepository<JobFairBoothLayoutEntity, String> {

    List<JobFairBoothLayoutEntity> findByJobFairBoothId(String companyBoothId);


    Optional<JobFairBoothLayoutEntity> findTopByJobFairBoothIdOrderByVersionDesc(String companyBoothId);
}
