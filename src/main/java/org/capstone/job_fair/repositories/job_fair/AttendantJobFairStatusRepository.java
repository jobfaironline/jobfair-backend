package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.AttendantJobFairStatusEntity;
import org.capstone.job_fair.models.statuses.JobFairAttendantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendantJobFairStatusRepository extends JpaRepository<AttendantJobFairStatusEntity, String> {
    Page<AttendantJobFairStatusEntity> getAllByAttendantId(String attendantId, Pageable pageable);

    Page<AttendantJobFairStatusEntity> getByStatusInAndAttendantId(List<JobFairAttendantStatus> status, String attendantId, Pageable pageable);


    @Query("select a from AttendantJobFairStatusEntity a " +
            "where a.status in ?1 and a.attendantId = ?2 and a.jobFair.name like concat('%', ?3, '%')")
    Page<AttendantJobFairStatusEntity> getByStatusInAndAttendantIdAndJobFairNameContaining(List<JobFairAttendantStatus> status, String attendantId, String JobFairName, Pageable pageable);

    Optional<AttendantJobFairStatusEntity> getByAttendantIdAndJobFairId(String attendantId, String jobFairId);
}
