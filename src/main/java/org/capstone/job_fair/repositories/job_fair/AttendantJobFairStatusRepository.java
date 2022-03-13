package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.AttendantJobFairStatusEntity;
import org.capstone.job_fair.models.statuses.JobFairAttendantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendantJobFairStatusRepository extends JpaRepository<AttendantJobFairStatusEntity, String> {
    Page<AttendantJobFairStatusEntity> getAllByAttendantId(String attendantId, Pageable pageable);

    Page<AttendantJobFairStatusEntity> getByStatusInAndAttendantId(List<JobFairAttendantStatus> status, String attendantId, Pageable pageable);
}
