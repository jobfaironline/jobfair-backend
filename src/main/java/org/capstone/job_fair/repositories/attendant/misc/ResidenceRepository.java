package org.capstone.job_fair.repositories.attendant.misc;

import org.capstone.job_fair.models.entities.attendant.misc.ResidenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ResidenceRepository extends JpaRepository<ResidenceEntity, Integer> {
    Integer countById(int id);
}
