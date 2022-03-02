package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.statuses.JobFairStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobFairRepository extends JpaRepository<JobFairEntity, String> {
    List<JobFairEntity> findAllByCreatorId(String id);

    Optional<JobFairEntity> findById(String id);

    List<JobFairEntity> findAllByStatus(JobFairStatus status);

    List<JobFairEntity> findAllByStatusAndCompanyRegisterStartTimeGreaterThanAndCompanyRegisterEndTimeLessThan(JobFairStatus status, Long startTime, Long endTime);

    Page<JobFairEntity> findByStatus(JobFairStatus status, Pageable pageable);

    Page<JobFairEntity> findAllByStatusNot(JobFairStatus status, Pageable pageable);
}
