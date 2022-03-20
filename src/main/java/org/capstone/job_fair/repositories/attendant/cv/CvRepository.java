package org.capstone.job_fair.repositories.attendant.cv;

import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CvRepository extends JpaRepository<CvEntity, String> {
    List<CvEntity> findByAttendantAccountId(String accountId);

    Optional<CvEntity> findByIdAndAttendantAccountId(String id, String attendantId);
}
