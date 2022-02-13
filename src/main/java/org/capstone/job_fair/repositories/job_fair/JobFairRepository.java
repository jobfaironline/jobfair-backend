package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.statuses.JobFairStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
/*
@RepositoryRestResource(path = ApiEndPoint.RestADataEndpoint.JOB_FAIR)
*/
@Repository
public interface JobFairRepository extends JpaRepository<JobFairEntity, String> {
    List<JobFairEntity> findAllByCreatorId(String id);

    Optional<JobFairEntity> findById(String id);

    List<JobFairEntity> findAllByStatus(JobFairStatus status);
}
