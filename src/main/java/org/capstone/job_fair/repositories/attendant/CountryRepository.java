package org.capstone.job_fair.repositories.attendant;

import org.capstone.job_fair.models.entities.attendant.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;


@RepositoryRestResource(path = "countries")
@Transactional
public interface CountryRepository extends JpaRepository<CountryEntity, String> {

}
