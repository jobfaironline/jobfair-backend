package org.capstone.job_fair.services.impl.job_fair.booth;

import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothLayoutDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothLayoutVideoDTO;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothLayoutEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothLayoutVideoEntity;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.JobFairBoothLayoutRepository;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.JobFairBoothLayoutVideoRepository;
import org.capstone.job_fair.services.interfaces.job_fair.booth.JobFairBoothLayoutService;
import org.capstone.job_fair.services.mappers.job_fair.booth.JobFairBoothLayoutMapper;
import org.capstone.job_fair.services.mappers.job_fair.booth.JobFairBoothLayoutVideoMapper;
import org.capstone.job_fair.utils.AwsUtil;
import org.capstone.job_fair.utils.GLTFUtil;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class JobFairBoothLayoutServiceImpl implements JobFairBoothLayoutService {

    @Autowired
    private JobFairBoothLayoutRepository jobFairBoothLayoutRepository;

    @Autowired
    private JobFairBoothLayoutVideoRepository jobFairBoothLayoutVideoRepository;

    @Autowired
    private JobFairBoothLayoutMapper boothLayoutMapper;

    @Autowired
    private JobFairBoothLayoutVideoMapper jobFairBoothLayoutVideoMapper;

    @Autowired
    private AwsUtil awsUtil;

    @Override
    public List<JobFairBoothLayoutDTO> getLayoutsByCompanyBoothId(String companyBoothId) {
        return jobFairBoothLayoutRepository
                .findByJobFairBoothId(companyBoothId)
                .stream()
                .map(boothLayoutMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<JobFairBoothLayoutDTO> getLatestVersionByCompanyBoothId(String companyBoothId) {
        return jobFairBoothLayoutRepository
                .findTopByJobFairBoothIdOrderByVersionDesc(companyBoothId)
                .map(boothLayoutMapper::toDTO);
    }

    @Override
    public Optional<JobFairBoothLayoutDTO> getById(String id) {
        return jobFairBoothLayoutRepository.findById(id).map(boothLayoutMapper::toDTO);
    }

    @Override
    @Transactional
    public JobFairBoothLayoutDTO createNew(JobFairBoothLayoutDTO dto, MultipartFile file) {
        try {
            GLTFUtil.parseAndValidateModel(file);
        } catch (IOException | UndeclaredThrowableException e) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Layout.INVALID_GLB_FILE));
        }

        Optional<JobFairBoothLayoutEntity> latestVersionOpt =
                jobFairBoothLayoutRepository.findTopByJobFairBoothIdOrderByVersionDesc(dto.getJobFairBooth().getId());
        int version = latestVersionOpt.map(jobFairBoothLayoutEntity -> jobFairBoothLayoutEntity.getVersion() + 1).orElse(0);


        JobFairBoothLayoutEntity entity = boothLayoutMapper.toEntity(dto);
        String id = UUID.randomUUID().toString();
        String url = awsUtil.generateAwsS3AccessString(AWSConstant.COMPANY_BOOTH_LAYOUT_FOLDER, id);
        entity.setId(id);
        entity.setUrl(url);
        entity.setVersion(version);
        entity.setCreateDate(new Date().getTime());
        entity = jobFairBoothLayoutRepository.save(entity);
        return boothLayoutMapper.toDTO(entity);
    }

    @Transactional
    @Override
    public JobFairBoothLayoutVideoDTO createNewVideoWithFile(JobFairBoothLayoutVideoDTO dto) {
        Optional<JobFairBoothLayoutEntity> layoutOpt = jobFairBoothLayoutRepository.findById(dto.getJobFairBoothLayoutId());
        if (!layoutOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Layout.NOT_FOUND));
        }

        JobFairBoothLayoutVideoEntity entity = jobFairBoothLayoutVideoMapper.toEntity(dto);
        String id = UUID.randomUUID().toString();
        String url = awsUtil.generateAwsS3AccessString(AWSConstant.COMPANY_BOOTH_LAYOUT_VIDEO_FOLDER, id);
        entity.setId(id);
        entity.setUrl(url);
        entity = jobFairBoothLayoutVideoRepository.save(entity);
        return jobFairBoothLayoutVideoMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public JobFairBoothLayoutVideoDTO createNewVideoWithUrl(JobFairBoothLayoutVideoDTO dto) {
        Optional<JobFairBoothLayoutEntity> layoutOpt = jobFairBoothLayoutRepository.findById(dto.getJobFairBoothLayoutId());
        if (!layoutOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Layout.NOT_FOUND));
        }
        JobFairBoothLayoutVideoEntity entity = jobFairBoothLayoutVideoMapper.toEntity(dto);
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        entity = jobFairBoothLayoutVideoRepository.save(entity);
        return jobFairBoothLayoutVideoMapper.toDTO(entity);
    }
}
