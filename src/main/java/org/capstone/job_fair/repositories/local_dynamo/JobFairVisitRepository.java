package org.capstone.job_fair.repositories.local_dynamo;

import org.capstone.job_fair.models.entities.dynamoDB.JobFairVisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobFairVisitRepository extends JpaRepository<JobFairVisitEntity, String> {
    List<JobFairVisitEntity> findByJobFairIdAndUserId(String jobFairId, String userId);

    List<JobFairVisitEntity> findByJobFairBoothIdAndUserId(String jobFairBoothId, String userId);

    long countByJobFairId(String jobFairId);
    long countByJobFairBoothId(String jobFairBoothId);
}
