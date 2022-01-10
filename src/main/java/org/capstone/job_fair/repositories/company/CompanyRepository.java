package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.COMPANY)
public interface CompanyRepository extends JpaRepository<CompanyEntity, String> {
}