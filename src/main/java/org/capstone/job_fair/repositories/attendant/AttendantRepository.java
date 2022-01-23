package org.capstone.job_fair.repositories.attendant;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Optional;

@Transactional
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.ATTENDANT)
public interface AttendantRepository extends JpaRepository<AttendantEntity, String> {
    List<AttendantEntity> findAll();
}
