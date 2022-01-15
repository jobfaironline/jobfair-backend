package org.capstone.job_fair.repositories.attendant.cv;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.CV)
@Transactional
public interface CvRepository extends JpaRepository<ApplicationEntity, String> {
}