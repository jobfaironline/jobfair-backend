package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.models.entities.company.JobFairBoothEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobFairBoothRepository extends JpaRepository<JobFairBoothEntity, String> {

    Optional<JobFairBoothEntity> findByJobFairIdAndBoothId(String jobFairId, String boothId);

    List<JobFairBoothEntity> findByJobFairId(String jobFairId);

    @Modifying
    Integer deleteAllByJobFairId(String jobFairId);

    Integer countByJobFairId(String jobFairId);

}
