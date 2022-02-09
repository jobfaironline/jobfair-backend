package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.company.CompanyRegistrationEntity;
import org.capstone.job_fair.models.statuses.CompanyRegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

@Transactional
@ApiIgnore
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.COMPANY_REGISTRATION, exported = false)
public interface CompanyRegistrationRepository extends JpaRepository<CompanyRegistrationEntity, String> {
    Optional<CompanyRegistrationEntity> findByCompanyIdAndStatus(String companyId, CompanyRegistrationStatus status);
}
