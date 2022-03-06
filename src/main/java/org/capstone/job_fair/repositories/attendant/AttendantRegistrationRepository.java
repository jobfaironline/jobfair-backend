package org.capstone.job_fair.repositories.attendant;

import org.capstone.job_fair.models.entities.attendant.AttendantRegistrationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendantRegistrationRepository extends JpaRepository<AttendantRegistrationEntity, String> {
    Page<AttendantRegistrationEntity> findAll(Pageable pageable);
}
