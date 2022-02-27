package org.capstone.job_fair.services.impl.job_fair;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.NamedModelElement;
import lombok.SneakyThrows;
import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.GLBConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.job_fair.LayoutDTO;
import org.capstone.job_fair.models.entities.job_fair.BoothEntity;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.entities.job_fair.LayoutEntity;
import org.capstone.job_fair.models.statuses.BoothStatus;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.repositories.job_fair.LayoutRepository;
import org.capstone.job_fair.services.interfaces.job_fair.LayoutService;
import org.capstone.job_fair.services.mappers.job_fair.LayoutMapper;
import org.capstone.job_fair.utils.AwsUtil;
import org.capstone.job_fair.utils.GLTFUtil;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LayoutServiceImpl implements LayoutService {

    @Autowired
    private LayoutRepository layoutRepository;

    @Autowired
    private JobFairRepository jobFairRepository;

    @Autowired
    private LayoutMapper layoutMapper;

    @Autowired
    private AwsUtil awsUtil;


    @Override
    public List<LayoutDTO> getAll() {
        return layoutRepository.findAll().stream().map(layoutMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<LayoutDTO> findById(String id) {
        return layoutRepository.findById(id).map(layoutMapper::toDTO);
    }

    @Override
    @Transactional
    public LayoutDTO createNew(LayoutDTO dto) {
        LayoutEntity entity = layoutMapper.toEntity(dto);
        String id = UUID.randomUUID().toString();
        String url = awsUtil.generateAwsS3AccessString(AWSConstant.LAYOUT_FOLDER, id);

        entity.setId(id);
        entity.setUrl(url);

        entity = layoutRepository.save(entity);
        return layoutMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public void update(LayoutDTO dto) {
        Optional<LayoutEntity> layoutEntityOpt = layoutRepository.findById(dto.getId());
        if (!layoutEntityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND));
        }
        LayoutEntity layoutEntity = layoutEntityOpt.get();
        layoutMapper.updateEntityFromDTO(dto, layoutEntity);
        layoutRepository.save(layoutEntity);
    }


    @Override
    @SneakyThrows
    @Transactional
    public void validateAndGenerateBoothSlot(MultipartFile file, String layoutId) {
        Optional<LayoutEntity> layoutEntityOpt = layoutRepository.findById(layoutId);
        if (!layoutEntityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Layout.NOT_FOUND));
        }
        LayoutEntity layoutEntity = layoutEntityOpt.get();
        GltfModel gltfModel;
        try {
            gltfModel = GLTFUtil.parseAndValidateModel(file);
        } catch (IOException | UndeclaredThrowableException e) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Layout.INVALID_GLB_FILE));
        }
        layoutEntity.getBooths().removeAll(layoutEntity.getBooths());
        Set<BoothEntity> boothEntities =
                gltfModel.getNodeModels().stream()
                        .map(NamedModelElement::getName)
                        .filter(name -> name.startsWith(GLBConstant.BOOTH_NAME_PREFIX))
                        .map(name -> {
                            BoothEntity boothEntity = new BoothEntity();
                            boothEntity.setId(UUID.randomUUID().toString());
                            boothEntity.setName(name);
                            boothEntity.setLayout(layoutEntity);
                            boothEntity.setStatus(BoothStatus.NORMAL);
                            boothEntity.setPrice(0.0);
                            return boothEntity;
                        })
                        .collect(Collectors.toSet());
        layoutEntity.getBooths().addAll(boothEntities);
        layoutRepository.save(layoutEntity);

    }

    @Override
    public Optional<LayoutDTO> findByJobFairId(String jobFairId) {
        System.out.println(jobFairId);
        Optional<JobFairEntity> jobFairEntityOpt = jobFairRepository.findById(jobFairId);
        if (!jobFairEntityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }
        JobFairEntity jobFairEntity = jobFairEntityOpt.get();
        System.out.println(jobFairEntity);
        return layoutRepository.findById(jobFairEntity.getLayoutId()).map(layoutMapper::toDTO);
    }
}
