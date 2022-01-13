package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.repositories.company.CompanyRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.interfaces.company.CompanySizeService;
import org.capstone.job_fair.services.mappers.CompanyEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {

    private static final String NOT_FOUND_SIZE = "Size not found with given Id";
    private static final String NOT_FOUND_COMPANY = "Company not found with given Id";

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanySizeService sizeService;

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
        return sizeService.findBySizeId(id).isPresent() ? sizeService.findBySizeId(id).get() : null;
    }

    @Override
    public CompanyEntity createCompany(CompanyDTO dto) {
        String id = UUID.randomUUID().toString();
        CompanyEntity entity = new CompanyEntity();
        mapper.DTOToEntity(dto, entity);
        entity.setId(id);

        CompanySizeEntity sizeEntity = findSizeById(dto.getSizeId());

        if (sizeEntity == null) {
            throw new NoSuchElementException(NOT_FOUND_SIZE);
        }

        entity.setCompanySize(sizeEntity);
        return companyRepository.save(entity);
    }

    @Transactional
    @Override
    public CompanyEntity updateCompany(CompanyDTO dto) {
        if (dto.getSizeId()!=null){
            CompanySizeEntity sizeEntity = findSizeById(dto.getSizeId());
            if (sizeEntity == null) {
                throw new NoSuchElementException(NOT_FOUND_SIZE);
            }
        }
        return companyRepository.findById(dto.getId()).map(com -> {
            mapper.DTOToEntity(dto, com);
            return companyRepository.save(com);
        }).orElse(null);
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

    @Override
    public Integer getCountByEmail(String email) {
        return companyRepository.countByEmail(email);
    }

    @Override
    public Integer getCountByTaxId(String taxId) {
        return companyRepository.countByTaxId(taxId);
    }
}
