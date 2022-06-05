package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.CompanyDTO;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    List<CompanyDTO> getAllCompanies();

    Optional<CompanyDTO> getCompanyById(String id);

    CompanyDTO createCompany(CompanyDTO dto);

    void updateCompany(CompanyDTO dto);

    Boolean deleteCompany(String id);

    Integer getCountById(String id);

    CompanyDTO updateCompanyLogo(String companyLogoFolder, String companyId);

    Optional<String> getIdByCompanyBoothID(String companyBoothId);
}
