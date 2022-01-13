package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.repositories.company.CompanyRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Optional<CompanyEntity> findCompanyById(String id) {
        return companyRepository.findById(id);
    }
}
