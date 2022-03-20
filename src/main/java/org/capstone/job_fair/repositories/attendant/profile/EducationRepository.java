package org.capstone.job_fair.repositories.attendant.profile;

import org.capstone.job_fair.models.entities.attendant.profile.EducationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends JpaRepository<EducationEntity, String> {
}