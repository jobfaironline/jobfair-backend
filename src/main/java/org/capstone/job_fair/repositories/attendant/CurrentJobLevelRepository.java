package org.capstone.job_fair.repositories.attendant;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.attendant.CurrentJobLevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.CURRENT_JOB_LEVEL)
public interface CurrentJobLevelRepository extends JpaRepository<CurrentJobLevelEntity, String> {
}
