package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.statuses.JobFairAdminStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminJobFairStatusRepository extends JpaRepository<AdminJobFairStatusEntity, String> {

    Page<AdminJobFairStatusEntity> getByStatusIn(List<JobFairAdminStatus> status, Pageable pageable);

    @Query("select a from AdminJobFairStatusEntity a where a.status in ?1 and a.jobFair.name like concat('%', ?2, '%')")
    Page<AdminJobFairStatusEntity> getByStatusInAndJobFairNameContaining(List<JobFairAdminStatus> status, String JobFairName, Pageable pageable);

    Optional<AdminJobFairStatusEntity> getByJobFairId(String jobfairId);
}
