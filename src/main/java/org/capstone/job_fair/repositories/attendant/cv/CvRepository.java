package org.capstone.job_fair.repositories.attendant.cv;

import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(path = "cvs")
@Transactional
public interface CvRepository extends JpaRepository<CvEntity, String> {
}