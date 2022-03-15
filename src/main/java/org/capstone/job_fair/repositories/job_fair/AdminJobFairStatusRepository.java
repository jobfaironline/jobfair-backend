package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.AdminJobFairStatusEntity;
import org.capstone.job_fair.models.statuses.JobFairAdminStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminJobFairStatusRepository extends JpaRepository<AdminJobFairStatusEntity, String> {

    Page<AdminJobFairStatusEntity> getByStatusIn(List<JobFairAdminStatus> status, Pageable pageable);

    Optional<AdminJobFairStatusEntity> getByJobFairId(String jobfairId);
}
