package org.capstone.job_fair.repositories.attendant.cv;

import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CvRepository extends JpaRepository<CvEntity, String> {
    List<CvEntity> findByAttendantAccountId(String accountId);

    @Query("select cv from CvEntity cv where (cv.name like :name or cv.name is null) and cv.attendant.accountId = :attendantId")
    Page<CvEntity> findByNameLikeOrNameIsNullAndAttendantId(@Param("name") String name, @Param("attendantId") String attendantId, Pageable pageable);

    Optional<CvEntity> findByIdAndAttendantAccountId(String id, String attendantId);

}
