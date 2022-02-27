package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.models.dtos.company.CompanyBoothDTO;
import org.capstone.job_fair.repositories.company.CompanyBoothRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyBoothService;
import org.capstone.job_fair.services.mappers.company.CompanyBoothMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CompanyBoothServiceImpl implements CompanyBoothService {
    @Autowired
    private CompanyBoothRepository companyBoothRepository;

    @Autowired
    private CompanyBoothMapper companyBoothMapper;

    @Override
    public Optional<CompanyBoothDTO> getCompanyBoothByJobFairIdAndBoothId(String jobFairId, String boothId) {
        return companyBoothRepository.getCompanyBoothByJobFairIdAndBoothId(jobFairId, boothId).map(companyBoothMapper::toDTO);
    }
}
