package org.capstone.job_fair.repositories.attendant.cv;

import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CvRepository extends JpaRepository<CvEntity, String> {
    List<CvEntity> findByAttendantAccountId(String accountId);
}
