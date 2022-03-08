package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.AdminJobFairStatusEntity;
import org.capstone.job_fair.models.statuses.JobFairAdminStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminJobFairStatusRepository extends JpaRepository<AdminJobFairStatusEntity, String> {

    Page<AdminJobFairStatusEntity> getByStatus(JobFairAdminStatus status, Pageable pageable);
}
