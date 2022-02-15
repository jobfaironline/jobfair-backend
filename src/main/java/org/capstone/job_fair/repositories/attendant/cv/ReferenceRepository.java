package org.capstone.job_fair.repositories.attendant.cv;

import org.capstone.job_fair.models.entities.attendant.cv.ReferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceRepository extends JpaRepository<ReferenceEntity, String> {
}