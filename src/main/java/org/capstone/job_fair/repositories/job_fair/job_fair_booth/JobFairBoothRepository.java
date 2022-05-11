package org.capstone.job_fair.repositories.job_fair.job_fair_booth;

import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobFairBoothRepository extends JpaRepository<JobFairBoothEntity, String> {

    Optional<JobFairBoothEntity> findByJobFairIdAndBoothId(String jobFairId, String boothId);

    List<JobFairBoothEntity> findByJobFairId(String jobFairId);

    @Modifying
    Integer deleteAllByJobFairId(String jobFairId);

    Integer countByJobFairId(String jobFairId);

    Optional<JobFairBoothEntity> findByIdAndJobFairCompanyId(String jobFairBoothId, String companyId);

}
