package org.capstone.job_fair.repositories.attendant;

import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendantRepository extends JpaRepository<AttendantEntity, String> {
    List<AttendantEntity> findAll();
}
