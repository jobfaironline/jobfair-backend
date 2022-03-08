package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobFairRepository extends JpaRepository<JobFairEntity, String> {

    Optional<JobFairEntity> findById(String id);

    List<JobFairEntity> findAllByStatus(JobFairPlanStatus status);

    List<JobFairEntity> findAllByStatusAndCompanyRegisterStartTimeGreaterThanAndCompanyRegisterEndTimeLessThan(JobFairPlanStatus status, Long startTime, Long endTime);

    Page<JobFairEntity> findAll(Pageable pageable);

    Page<JobFairEntity> findByStatus(JobFairPlanStatus status, Pageable pageable);

    Page<JobFairEntity> findAllByStatusNot(JobFairPlanStatus status, Pageable pageable);

    Page<JobFairEntity> findAllByCreatorIdAndStatus(String creatorId, JobFairPlanStatus status, Pageable pageable);

    Page<JobFairEntity> findAllByCreatorId(String creatorId, Pageable pageable);
}
