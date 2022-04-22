package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobFairRepository extends JpaRepository<JobFairEntity, String> {
    Optional<JobFairEntity> findByIdAndCompanyId(String jobFairId, String companyId);

    List<JobFairEntity> findByCompanyIdAndStatus(String companyId, JobFairPlanStatus status);

    @Query("select j from JobFairEntity j where (j.name like ?1 or j.name is null) and j.company.id = ?2")
    Page<JobFairEntity> findByNameLikeOrNameIsNullAndCompanyId(String name, String companyId, Pageable pageable);


}
