package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.company.JobFairBoothDTO;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.repositories.company.CompanyBoothRepository;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyBoothService;
import org.capstone.job_fair.services.mappers.company.CompanyBoothMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CompanyBoothServiceImpl implements CompanyBoothService {
    @Autowired
    private CompanyBoothRepository companyBoothRepository;

    @Autowired
    private JobFairRepository jobFairRepository;

    @Autowired
    private CompanyBoothMapper companyBoothMapper;

    @Override
    public Optional<JobFairBoothDTO> getCompanyBoothByJobFairIdAndBoothId(String jobFairId, String boothId) {
        return companyBoothRepository.getCompanyBoothByJobFairIdAndBoothId(jobFairId, boothId).map(companyBoothMapper::toDTO);
    }

    @Override
    public List<JobFairBoothDTO> getCompanyBoothByJobFairIdAndCompanyId(String jobFairId, String companyId) {
        Optional<JobFairEntity> jobFairOpt = jobFairRepository.findById(jobFairId);
        if (!jobFairOpt.isPresent()){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }
        return companyBoothRepository.getCompanyBoothByJobFairIdAndCompanyId(jobFairId, companyId)
                .stream()
                .map(companyBoothMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<JobFairBoothDTO> getById(String boothId) {
        return companyBoothRepository.findById(boothId).map(companyBoothMapper::toDTO);
    }
}
