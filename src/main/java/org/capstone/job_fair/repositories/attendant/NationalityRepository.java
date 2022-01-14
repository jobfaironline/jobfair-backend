package org.capstone.job_fair.repositories.attendant;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.attendant.NationalityEntity;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.NATIONALITY)
public interface NationalityRepository extends JpaRepository<NationalityEntity, String> {
    Integer countById(String id);

}
