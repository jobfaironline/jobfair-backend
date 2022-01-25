package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.COMPANY_EMPLOYEE, exported = false)
public interface CompanyEmployeeRepository extends JpaRepository<CompanyEmployeeEntity, String> {

   @Query("select c from CompanyEmployeeEntity c where c.company.id = ?1")
   List<CompanyEmployeeEntity> findAllByCompanyId (String id);
   @Query("select c FROM CompanyEmployeeEntity c WHERE c.accountId = ?1")
   Optional<CompanyEmployeeEntity> findByAccountId(String id);



}