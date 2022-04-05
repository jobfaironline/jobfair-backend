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

    Optional<JobFairEntity> findById(String id);

    List<JobFairEntity> findAllByStatus(JobFairPlanStatus status);


    Page<JobFairEntity> findAll(Pageable pageable);

    Page<JobFairEntity> findByStatus(JobFairPlanStatus status, Pageable pageable);

    @Query("select j from JobFairEntity j where j.status = ?1 and j.name like concat('%', ?2, '%')")
    Page<JobFairEntity> findByStatusAndNameContaining(JobFairPlanStatus status, String name, Pageable pageable);

    Page<JobFairEntity> findAllByStatusNot(JobFairPlanStatus status, Pageable pageable);


    List<JobFairEntity> findAllByNameContains(String name);

}
