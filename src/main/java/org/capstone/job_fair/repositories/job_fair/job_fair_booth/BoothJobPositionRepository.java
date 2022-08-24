package org.capstone.job_fair.repositories.job_fair.job_fair_booth;

import org.capstone.job_fair.models.entities.job_fair.booth.BoothJobPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoothJobPositionRepository extends JpaRepository<BoothJobPositionEntity, String> {


    List<BoothJobPositionEntity> findByJobFairBoothJobFairId(String jobFairId);
}
