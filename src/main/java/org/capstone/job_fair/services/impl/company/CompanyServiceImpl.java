package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.company.CompanySizeEntity;
import org.capstone.job_fair.repositories.company.CompanyRepository;
import org.capstone.job_fair.repositories.company.CompanySizeRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.mappers.CompanyEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanySizeRepository sizeRepository;

    @Autowired
    private CompanyEntityMapper mapper;

    @Override
    public List<CompanyEntity> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Optional<CompanyEntity> getCompanyById(String id) {
        return companyRepository.findById(id);
    }

    private CompanySizeEntity findSizeById(String id) {
        return sizeRepository.findById(id).isPresent() ? sizeRepository.findById(id).get() : null;
    }

    @Override
    public void createCompany(CompanyDTO dto) {
        String id = UUID.randomUUID().toString();
        CompanyEntity entity = mapper.toEntity(dto);
        entity.setId(id);

        CompanySizeEntity sizeEntity = findSizeById(dto.getSizeId());

        if (sizeEntity != null) {
            entity.setCompanySize(sizeEntity);
        }

        companyRepository.save(entity);
    }

    @Transactional
    @Override
    public Boolean updateCompany(String id, CompanyDTO dto) {
        Optional<CompanyEntity> result = this.getCompanyById(id);
        if (result.isPresent()) {
            CompanyEntity com = result.get();
            com.setName(dto.getName().trim());
            com.setAddress(dto.getAddress().trim());
            com.setPhone(dto.getPhone().trim());
            com.setEmail(dto.getEmail().trim());
            com.setEmployeeMaxNum(dto.getEmployeeMaxNum());
            com.setWebsiteUrl(dto.getWebsiteUrl().trim());

            CompanySizeEntity sizeEntity = findSizeById(dto.getSizeId());
            if (sizeEntity != null) {
                com.setCompanySize(sizeEntity);
                companyRepository.save(com);
                return true;
            }
        }
        return false;
    }

    @Transactional
    @Override
    public Boolean deleteCompany(String id) {
        Optional<CompanyEntity> result = this.getCompanyById(id);
        if (result.isPresent()) {
            companyRepository.delete(result.get());
            return true;
        }
        return false;
    }

    @Override
    public CompanyEntity findByTaxId(String taxId) {
        Optional<CompanyEntity> result = companyRepository.findByTaxId(taxId);
        return result.isPresent() ? result.get() : null;
    }

}
