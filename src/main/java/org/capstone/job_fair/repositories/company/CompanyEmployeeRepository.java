package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.repositories.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.COMPANY_EMPLOYEE, exported = false)
public interface CompanyEmployeeRepository extends JpaRepository<CompanyEmployeeEntity, String>, CustomRepository<CompanyEmployeeEntity, String> {

   List<CompanyEmployeeEntity> findAllByCompanyId (String id);

}