package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.company.JobFairBoothLayoutDTO;
import org.capstone.job_fair.models.dtos.company.JobFairBoothLayoutVideoDTO;
import org.capstone.job_fair.models.entities.company.JobFairBoothLayoutEntity;
import org.capstone.job_fair.models.entities.company.JobFairBoothLayoutVideoEntity;
import org.capstone.job_fair.repositories.company.CompanyBoothLayoutRepository;
import org.capstone.job_fair.repositories.company.CompanyBoothLayoutVideoRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyBoothLayoutService;
import org.capstone.job_fair.services.mappers.company.CompanyBoothLayoutMapper;
import org.capstone.job_fair.services.mappers.company.CompanyBoothLayoutVideoMapper;
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
public class CompanyBoothLayoutServiceImpl implements CompanyBoothLayoutService {

    @Autowired
    private CompanyBoothLayoutRepository companyBoothLayoutRepository;

    @Autowired
    private CompanyBoothLayoutVideoRepository companyBoothLayoutVideoRepository;

    @Autowired
    private CompanyBoothLayoutMapper boothLayoutMapper;

    @Autowired
    private CompanyBoothLayoutVideoMapper companyBoothLayoutVideoMapper;

    @Autowired
    private AwsUtil awsUtil;

    @Override
    public List<JobFairBoothLayoutDTO> getLayoutsByCompanyBoothId(String companyBoothId) {
        return companyBoothLayoutRepository
                .findByJobFairBoothId(companyBoothId)
                .stream()
                .map(boothLayoutMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<JobFairBoothLayoutDTO> getLatestVersionByCompanyBoothId(String companyBoothId) {
        return companyBoothLayoutRepository
                .findTopByJobFairBoothIdOrderByVersionDesc(companyBoothId)
                .map(boothLayoutMapper::toDTO);
    }

    @Override
    public Optional<JobFairBoothLayoutDTO> getById(String id) {
        return companyBoothLayoutRepository.findById(id).map(boothLayoutMapper::toDTO);
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
                    companyBoothLayoutRepository.findTopByJobFairBoothIdOrderByVersionDesc(dto.getJobFairBooth().getId());
        int version = latestVersionOpt.map(jobFairBoothLayoutEntity -> jobFairBoothLayoutEntity.getVersion() + 1).orElse(0);



        JobFairBoothLayoutEntity entity = boothLayoutMapper.toEntity(dto);
        String id = UUID.randomUUID().toString();
        String url = awsUtil.generateAwsS3AccessString(AWSConstant.COMPANY_BOOTH_LAYOUT_FOLDER, id);
        entity.setId(id);
        entity.setUrl(url);
        entity.setVersion(version);
        entity.setCreateDate(new Date().getTime());
        entity = companyBoothLayoutRepository.save(entity);
        return boothLayoutMapper.toDTO(entity);
    }

    @Transactional
    @Override
    public JobFairBoothLayoutVideoDTO createNewVideoWithFile(JobFairBoothLayoutVideoDTO dto){
        Optional<JobFairBoothLayoutEntity> layoutOpt = companyBoothLayoutRepository.findById(dto.getJobFairBoothLayoutId());
        if (!layoutOpt.isPresent()){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Layout.NOT_FOUND));
        }

        JobFairBoothLayoutVideoEntity entity = companyBoothLayoutVideoMapper.toEntity(dto);
        String id = UUID.randomUUID().toString();
        String url = awsUtil.generateAwsS3AccessString(AWSConstant.COMPANY_BOOTH_LAYOUT_VIDEO_FOLDER, id);
        entity.setId(id);
        entity.setUrl(url);
        entity = companyBoothLayoutVideoRepository.save(entity);
        return companyBoothLayoutVideoMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public JobFairBoothLayoutVideoDTO createNewVideoWithUrl(JobFairBoothLayoutVideoDTO dto) {
        Optional<JobFairBoothLayoutEntity> layoutOpt = companyBoothLayoutRepository.findById(dto.getJobFairBoothLayoutId());
        if (!layoutOpt.isPresent()){
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Layout.NOT_FOUND));
        }
        JobFairBoothLayoutVideoEntity entity = companyBoothLayoutVideoMapper.toEntity(dto);
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        entity = companyBoothLayoutVideoRepository.save(entity);
        return companyBoothLayoutVideoMapper.toDTO(entity);
    }
}
