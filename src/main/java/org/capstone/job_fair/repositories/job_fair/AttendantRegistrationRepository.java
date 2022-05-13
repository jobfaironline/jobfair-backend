package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.AttendantRegistrationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendantRegistrationRepository extends JpaRepository<AttendantRegistrationEntity, String> {
    Page<AttendantRegistrationEntity> findAll(Pageable pageable);

    Optional<AttendantRegistrationEntity> findByAttendantIdAndJobFairId(String attendantId, String jobFairId);
}
