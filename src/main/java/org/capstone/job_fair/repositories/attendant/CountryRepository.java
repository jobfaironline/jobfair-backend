package org.capstone.job_fair.repositories.attendant;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.attendant.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;


@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.COUNTRY)
@Transactional
public interface CountryRepository extends JpaRepository<CountryEntity, String> {
    Integer countById(String id);
}
