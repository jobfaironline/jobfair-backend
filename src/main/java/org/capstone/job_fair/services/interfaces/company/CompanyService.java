package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.CompanyRegistrationDTO;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.entities.company.CompanyEntity;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    List<CompanyDTO> getAllCompanies();

    Optional<CompanyEntity> getCompanyById(String id);

    void createCompany(CompanyDTO dto);

    void updateCompany(CompanyDTO dto);

    Boolean deleteCompany(String id);

    Integer getCountById(String id);

    Optional<CompanyEntity> findCompanyById(String id);

    void createDraftCompanyRegistration(CompanyRegistrationDTO companyRegistrationDTO, List<RegistrationJobPositionDTO> jobPositions);
}
