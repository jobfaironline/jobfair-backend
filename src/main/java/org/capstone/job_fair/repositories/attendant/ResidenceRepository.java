package org.capstone.job_fair.repositories.attendant;

import org.capstone.job_fair.models.entities.attendant.ResidenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ResidenceRepository extends JpaRepository<ResidenceEntity, Integer> {
    Integer countById(int id);
}
