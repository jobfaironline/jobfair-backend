package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.AttendantJobFairStatusEntity;
import org.capstone.job_fair.models.entities.job_fair.CompanyJobFairStatusEntity;
import org.capstone.job_fair.models.statuses.JobFairAttendantStatus;
import org.capstone.job_fair.models.statuses.JobFairCompanyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendantJobFairStatusRepository extends JpaRepository<AttendantJobFairStatusEntity, String> {
    Page<AttendantJobFairStatusEntity> getAllByAttendantId(String attendantId, Pageable pageable);

    Page<AttendantJobFairStatusEntity> getByStatusAndAttendantId(JobFairAttendantStatus status, String attendantId, Pageable pageable);
}
