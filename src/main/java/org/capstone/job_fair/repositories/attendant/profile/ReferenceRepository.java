package org.capstone.job_fair.repositories.attendant.profile;

import org.capstone.job_fair.models.entities.attendant.profile.ReferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceRepository extends JpaRepository<ReferenceEntity, String> {
}