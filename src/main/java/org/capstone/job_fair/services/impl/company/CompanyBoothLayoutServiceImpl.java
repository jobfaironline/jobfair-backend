package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.company.CompanyBoothLayoutDTO;
import org.capstone.job_fair.models.entities.company.CompanyBoothLayoutEntity;
import org.capstone.job_fair.repositories.company.CompanyBoothLayoutRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyBoothLayoutService;
import org.capstone.job_fair.services.mappers.company.CompanyBoothLayoutMapper;
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
    private CompanyBoothLayoutMapper boothLayoutMapper;

    @Autowired
    private AwsUtil awsUtil;

    @Override
    public List<CompanyBoothLayoutDTO> getLayoutsByCompanyBoothId(String companyBoothId) {
        return companyBoothLayoutRepository
                .findByCompanyBoothId(companyBoothId)
                .stream()
                .map(boothLayoutMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CompanyBoothLayoutDTO> getLatestVersionByCompanyBoothId(String companyBoothId) {
        return companyBoothLayoutRepository
                .findTopByCompanyBoothIdOrderByVersionDesc(companyBoothId)
                .map(boothLayoutMapper::toDTO);
    }

    @Override
    public Optional<CompanyBoothLayoutDTO> getById(String id) {
        return companyBoothLayoutRepository.findById(id).map(boothLayoutMapper::toDTO);
    }

    @Override
    @Transactional
    public CompanyBoothLayoutDTO createNew(CompanyBoothLayoutDTO dto, MultipartFile file) {
        try {
            GLTFUtil.parseAndValidateModel(file);
        } catch (IOException | UndeclaredThrowableException e) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Layout.INVALID_GLB_FILE));
        }

        Optional<CompanyBoothLayoutEntity> latestVersionOpt =
                companyBoothLayoutRepository.findTopByCompanyBoothIdOrderByVersionDesc(dto.getCompanyBooth().getId());
        int version = latestVersionOpt.map(companyBoothLayoutEntity -> companyBoothLayoutEntity.getVersion() + 1).orElse(0);



        CompanyBoothLayoutEntity entity = boothLayoutMapper.toEntity(dto);
        String id = UUID.randomUUID().toString();
        String url = awsUtil.generateAwsS3AccessString(AWSConstant.COMPANY_BOOTH_LAYOUT_FOLDER, id);
        entity.setId(id);
        entity.setUrl(url);
        entity.setVersion(version);
        entity.setCreateDate(new Date().getTime());
        entity = companyBoothLayoutRepository.save(entity);
        return boothLayoutMapper.toDTO(entity);
    }
}