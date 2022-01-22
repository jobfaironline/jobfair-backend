package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.repositories.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.COMPANY)
public interface CompanyRepository extends JpaRepository<CompanyEntity, String>, CustomRepository<CompanyEntity, String> {
    Optional<CompanyEntity> findByTaxId(String taxId);
    Integer countByEmail(String email);
    Integer countByTaxId(String taxId);
    Integer countById(String id);
    Integer countByIdAndTaxId(String id, String taxId);
}