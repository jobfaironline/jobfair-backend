package org.capstone.job_fair.repositories.job_fair.job_fair_booth;

import org.capstone.job_fair.models.entities.job_fair.booth.BoothJobPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothJobPositionRepository extends JpaRepository<BoothJobPositionEntity, String> {

    Integer countById(String id);
}
