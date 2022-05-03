package org.capstone.job_fair.repositories.attendant;

import org.capstone.job_fair.models.entities.attendant.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Integer> {
    Integer countById(int id);
}
