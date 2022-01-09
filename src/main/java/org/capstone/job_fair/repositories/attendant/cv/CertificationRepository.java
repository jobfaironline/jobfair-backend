package org.capstone.job_fair.repositories.attendant.cv;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.attendant.cv.CertificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.CERTIFICATION)
public interface CertificationRepository extends JpaRepository<CertificationEntity, String> {
}