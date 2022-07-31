package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    Page<CompanyDTO> getAllCompanies(String searchValue, int pageSize, int offset, String sortBy, Sort.Direction direction);

    Optional<CompanyDTO> getCompanyById(String id);

    CompanyDTO createCompany(CompanyDTO dto);

    void updateCompany(CompanyDTO dto);

    Boolean deleteCompany(String id);

    Integer getCountById(String id);

    CompanyDTO updateCompanyLogo(String companyLogoFolder, String companyId);

}
