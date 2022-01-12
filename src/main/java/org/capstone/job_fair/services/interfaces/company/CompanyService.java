package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.entities.company.CompanyEntity;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    List<CompanyEntity> getAllCompanies();
    Optional<CompanyEntity> getCompanyById(String id);
    void createCompany(CompanyDTO dto);
    Boolean updateCompany(String id, CompanyDTO dto) ;
    Boolean deleteCompany(String id);
    CompanyEntity findByTaxId(String taxId);
}
