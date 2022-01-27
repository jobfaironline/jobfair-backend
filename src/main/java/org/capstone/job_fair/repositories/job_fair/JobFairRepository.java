package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.JOB_FAIR)
public interface JobFairRepository extends JpaRepository<JobFairEntity, String> {
    List<JobFairEntity> findAllByCreatorId(String id);
    Optional<JobFairEntity> findById(String id);
}
