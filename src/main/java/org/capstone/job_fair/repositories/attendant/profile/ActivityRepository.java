package org.capstone.job_fair.repositories.attendant.profile;

import org.capstone.job_fair.models.entities.attendant.profile.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, String> {
}
