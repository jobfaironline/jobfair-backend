package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.BoothEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothRepository extends JpaRepository<BoothEntity, String> {
}
