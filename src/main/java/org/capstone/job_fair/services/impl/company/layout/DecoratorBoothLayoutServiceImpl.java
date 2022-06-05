package org.capstone.job_fair.services.impl.company.layout;

import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.company.layout.DecoratorBoothLayoutDTO;
import org.capstone.job_fair.models.dtos.company.layout.DecoratorBoothLayoutVideoDTO;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.entities.company.layout.DecoratorBoothLayoutEntity;
import org.capstone.job_fair.models.entities.company.layout.DecoratorBoothLayoutVideoEntity;
import org.capstone.job_fair.repositories.company.layout.DecorateBoothLayoutVideoRepository;
import org.capstone.job_fair.repositories.company.layout.DecoratorBoothLayoutRepository;
import org.capstone.job_fair.services.interfaces.company.layout.DecoratorBoothLayoutService;
import org.capstone.job_fair.services.mappers.company.layout.DecoratorBoothLayoutMapper;
import org.capstone.job_fair.services.mappers.company.layout.DecoratorBoothLayoutVideoMapper;
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
public class DecoratorBoothLayoutServiceImpl implements DecoratorBoothLayoutService {
    @Autowired
    private DecorateBoothLayoutVideoRepository decorateBoothLayoutVideoRepository;

    @Autowired
    private DecoratorBoothLayoutRepository decoratorBoothLayoutRepository;

    @Autowired
    private DecoratorBoothLayoutMapper decoratorBoothLayoutMapper;

    @Autowired
    private DecoratorBoothLayoutVideoMapper decoratorBoothLayoutVideoMapper;

    @Autowired
    private AwsUtil awsUtil;

    @Override
    public List<DecoratorBoothLayoutDTO> getLayoutsByCompanyEmployeeId(String employeeId) {
        return decoratorBoothLayoutRepository.findByCompanyEmployeeAccountId(employeeId)
                .stream().map(decoratorBoothLayoutMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<DecoratorBoothLayoutDTO> getById(String id) {
        return decoratorBoothLayoutRepository.findById(id).map(decoratorBoothLayoutMapper::toDTO);
    }

    @Override
    @Transactional
    public DecoratorBoothLayoutDTO create(String companyEmployeeId, String name, MultipartFile file) {
        try {
            GLTFUtil.parseAndValidateModel(file);
        } catch (IOException | UndeclaredThrowableException e) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Layout.INVALID_GLB_FILE));
        }
        DecoratorBoothLayoutEntity entity = new DecoratorBoothLayoutEntity();
        String id = UUID.randomUUID().toString();
        String url = awsUtil.generateAwsS3AccessString(AWSConstant.DECORATOR_LAYOUT_FOLDER, id);
        entity.setId(id);
        entity.setUrl(url);
        entity.setCreateTime(new Date().getTime());
        entity.setName(name);

        CompanyEmployeeEntity companyEmployeeEntity = new CompanyEmployeeEntity();
        companyEmployeeEntity.setAccountId(companyEmployeeId);
        entity.setCompanyEmployee(companyEmployeeEntity);

        entity = decoratorBoothLayoutRepository.save(entity);
        return decoratorBoothLayoutMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public DecoratorBoothLayoutVideoDTO createNewVideoWithFile(String layoutId, String itemName) {
        Optional<DecoratorBoothLayoutEntity> layoutOpt = decoratorBoothLayoutRepository.findById(layoutId);
        if (!layoutOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Layout.NOT_FOUND));
        }

        String id = UUID.randomUUID().toString();
        String url = awsUtil.generateAwsS3AccessString(AWSConstant.DECORATOR_BOOTH_LAYOUT_VIDEO_FOLDER, id);

        DecoratorBoothLayoutVideoEntity entity = new DecoratorBoothLayoutVideoEntity();
        entity.setId(id);
        entity.setUrl(url);
        entity.setItemName(itemName);

        entity = decorateBoothLayoutVideoRepository.save(entity);
        return decoratorBoothLayoutVideoMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public DecoratorBoothLayoutVideoDTO createNewVideoWithUrl(String layoutId, String itemName, String url) {
        Optional<DecoratorBoothLayoutEntity> layoutOpt = decoratorBoothLayoutRepository.findById(layoutId);
        if (!layoutOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Layout.NOT_FOUND));
        }

        String id = UUID.randomUUID().toString();
        DecoratorBoothLayoutVideoEntity entity = new DecoratorBoothLayoutVideoEntity();
        entity.setId(id);
        entity.setUrl(url);
        entity.setItemName(itemName);

        entity = decorateBoothLayoutVideoRepository.save(entity);
        return decoratorBoothLayoutVideoMapper.toDTO(entity);
    }
}
