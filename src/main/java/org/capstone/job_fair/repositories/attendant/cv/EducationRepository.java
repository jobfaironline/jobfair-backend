package org.capstone.job_fair.repositories.attendant.cv;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.attendant.cv.EducationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.EDUCATION)
@Transactional
public interface EducationRepository extends JpaRepository<EducationEntity, String> {
}