package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.company.JobFairBoothDTO;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.repositories.company.JobFairBoothRepository;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.services.interfaces.company.JobFairBoothService;
import org.capstone.job_fair.services.mappers.company.JobFairBoothMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class JobFairBoothServiceImpl implements JobFairBoothService {
    @Autowired
    private JobFairBoothRepository jobFairBoothRepository;

    @Autowired
    private JobFairRepository jobFairRepository;

    @Autowired
    private JobFairBoothMapper jobFairBoothMapper;

    @Override
    public Optional<JobFairBoothDTO> getCompanyBoothByJobFairIdAndBoothId(String jobFairId, String boothId) {
        return jobFairBoothRepository.findByJobFairIdAndBoothId(jobFairId, boothId).map(jobFairBoothMapper::toDTO);
    }

    @Override
    public List<JobFairBoothDTO> getCompanyBoothByJobFairId(String jobFairId) {
        Optional<JobFairEntity> jobFairOpt = jobFairRepository.findById(jobFairId);
        if (!jobFairOpt.isPresent()){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }
        return jobFairBoothRepository.findByJobFairId(jobFairId)
                .stream()
                .map(jobFairBoothMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<JobFairBoothDTO> getById(String boothId) {
        return jobFairBoothRepository.findById(boothId).map(jobFairBoothMapper::toDTO);
    }

    @Override
    public Integer getBoothCountByJobFair(String jobFairId) {
        return jobFairBoothRepository.countByJobFairId(jobFairId);
    }
}
