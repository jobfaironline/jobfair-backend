package org.capstone.job_fair.repositories.attendant;

import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

@Transactional
@RepositoryRestResource(path = "attendants")
public interface AttendantRepository extends JpaRepository<AttendantEntity, String> {

    @ApiIgnore
    @RestResource(exported = false)
    Optional<AttendantEntity> findAccountByEmailAndStatusNot(String email, int status);

}
