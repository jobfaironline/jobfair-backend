package org.capstone.job_fair.services.impl.job_fair.booth;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.job_fair.booth.BoothJobPositionDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothDTO;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothEntity;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.capstone.job_fair.repositories.company.job.JobPositionRepository;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.JobFairBoothRepository;
import org.capstone.job_fair.services.interfaces.job_fair.booth.JobFairBoothService;
import org.capstone.job_fair.services.mappers.job_fair.booth.JobFairBoothMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    @Autowired
    private JobPositionRepository jobPositionRepository;

    @Override
    public Optional<JobFairBoothDTO> getCompanyBoothByJobFairIdAndBoothId(String jobFairId, String boothId) {
        return jobFairBoothRepository.findByJobFairIdAndBoothId(jobFairId, boothId).map(jobFairBoothMapper::toDTO);
    }

    @Override
    public List<JobFairBoothDTO> getCompanyBoothByJobFairId(String jobFairId) {
        Optional<JobFairEntity> jobFairOpt = jobFairRepository.findById(jobFairId);
        if (!jobFairOpt.isPresent()) {
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


    private void validateUniqueJobPosition(List<BoothJobPositionDTO> jobPositions) {
        jobPositions.sort(Comparator.comparing(BoothJobPositionDTO::getOriginJobPosition));
        for (int i = 0; i <= jobPositions.size() - 2; i++) {
            BoothJobPositionDTO currentDTO = jobPositions.get(i);
            BoothJobPositionDTO nextDTO = jobPositions.get(i + 1);
            if (currentDTO.getOriginJobPosition().equals(nextDTO.getOriginJobPosition())) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFairBooth.UNIQUE_JOB_POSITION_ERROR));
            }
        }
    }

    @Override
    @Transactional
    public JobFairBoothDTO updateJobFairBooth(JobFairBoothDTO jobFairBooth, String companyId) {
        //check for valid job fair booth
        Optional<JobFairBoothEntity> jobFairBoothOpt = jobFairBoothRepository.findByIdAndJobFairCompanyId(jobFairBooth.getId(), companyId);
        if (!jobFairBoothOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFairBooth.NOT_FOUND));
        }
        JobFairBoothEntity jobFairBoothEntity = jobFairBoothOpt.get();
        //check unique job position
        validateUniqueJobPosition(jobFairBooth.getBoothJobPositions());
        //check job fair status
        JobFairEntity jobFairEntity = jobFairBoothEntity.getJobFair();
        if (jobFairEntity.getStatus() == JobFairPlanStatus.DRAFT) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.NOT_EDITABLE));
        }
        //check decorate time
        long now = new Date().getTime();
        if (now < jobFairEntity.getDecorateStartTime() || now > jobFairEntity.getDecorateEndTime()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.NOT_EDITABLE));
        }
        //check job position belongs in company
        jobFairBooth.getBoothJobPositions().forEach(boothJobPositionDTO -> {
            Optional<JobPositionEntity> jobPositionOpt = jobPositionRepository.findById(boothJobPositionDTO.getOriginJobPosition());
            if (!jobPositionOpt.isPresent()) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.JOB_POSITION_NOT_FOUND));
            }
            JobPositionEntity jobPosition = jobPositionOpt.get();
            if (!jobPosition.getCompany().getId().equals(companyId)) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.COMPANY_MISMATCH));
            }
        });


        jobFairBoothMapper.updateEntity(jobFairBooth, jobFairBoothEntity);
        jobFairBoothEntity.getBoothJobPositions().forEach(boothPosition -> {
            Optional<JobPositionEntity> jobPositionOpt = jobPositionRepository.findById(boothPosition.getOriginJobPosition());
            JobPositionEntity jobPositionEntity = jobPositionOpt.get();
            boothPosition.setTitle(jobPositionEntity.getTitle());
            boothPosition.setContactEmail(jobPositionEntity.getContactEmail());
            boothPosition.setContactPersonName(jobPositionEntity.getContactPersonName());
            boothPosition.setLanguage(jobPositionEntity.getLanguage());
            boothPosition.setJobLevel(jobPositionEntity.getJobLevel());
            boothPosition.setJobTypeEntity(jobPositionEntity.getJobTypeEntity());
            boothPosition.setCategories(new HashSet<>(jobPositionEntity.getCategories()));
            boothPosition.setSkillTagEntities(new HashSet<>(jobPositionEntity.getSkillTagEntities()));
            boothPosition.setDescription(jobPositionEntity.getDescription());
            boothPosition.setRequirements(jobPositionEntity.getRequirements());
        });
        jobFairBoothRepository.save(jobFairBoothEntity);
        return jobFairBooth;
    }
}
