package org.capstone.job_fair.repositories.attendant.cv;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.attendant.cv.ReferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.REFERENCE)
public interface ReferenceRepository extends JpaRepository<ReferenceEntity, String> {
}