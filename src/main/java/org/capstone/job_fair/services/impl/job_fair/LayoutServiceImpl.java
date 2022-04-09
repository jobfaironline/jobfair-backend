package org.capstone.job_fair.services.impl.job_fair;

import de.javagl.jgltf.model.GltfModel;
import lombok.SneakyThrows;
import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.GLBConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.job_fair.LayoutBoothDTO;
import org.capstone.job_fair.models.dtos.job_fair.LayoutDTO;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.entities.job_fair.LayoutBoothEntity;
import org.capstone.job_fair.models.entities.job_fair.LayoutEntity;
import org.capstone.job_fair.models.statuses.BoothStatus;
import org.capstone.job_fair.repositories.company.JobFairBoothRepository;
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

    @Autowired
    private JobFairBoothRepository jobFairBoothRepository;


    @Override
    public List<LayoutDTO> getAllTemplateLayout() {
        return layoutRepository.findByCompanyIdIsNull().stream().map(layoutMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<LayoutDTO> getCompanyLayout(String companyId) {
        return layoutRepository.findByCompanyId(companyId).stream().map(layoutMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<LayoutDTO> findByIdAndCompanyId(String id, String companyId) {
        if (companyId == null) {
            return layoutRepository.findById(id).map(layoutMapper::toDTO);
        }
        return layoutRepository.findByIdAndCompanyId(id, companyId).map(layoutMapper::toDTO);
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
    public LayoutDTO update(LayoutDTO dto) {
        Optional<LayoutEntity> layoutEntityOpt;
        if (dto.getCompany() == null){
            layoutEntityOpt = layoutRepository.findById(dto.getId());
        } else {
            layoutEntityOpt = layoutRepository.findByIdAndCompanyId(dto.getId(), dto.getCompany().getId());
        }
        if (!layoutEntityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND));
        }
        LayoutEntity layoutEntity = layoutEntityOpt.get();
        layoutMapper.updateEntityFromDTO(dto, layoutEntity);
        layoutEntity = layoutRepository.save(layoutEntity);
        return layoutMapper.toDTO(layoutEntity);
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
        Set<LayoutBoothEntity> boothEntities =
                gltfModel.getNodeModels().stream()
                        .filter(nodeModel -> nodeModel.getName().startsWith(GLBConstant.BOOTH_NAME_PREFIX))
                        .map(nodeModel -> {
                            LayoutBoothEntity layoutBoothEntity = new LayoutBoothEntity();
                            layoutBoothEntity.setId(UUID.randomUUID().toString());
                            layoutBoothEntity.setName(nodeModel.getName().replaceAll("([._\\-])", ""));
                            layoutBoothEntity.setLayout(layoutEntity);
                            layoutBoothEntity.setStatus(BoothStatus.NORMAL);

                            //get position
                            //https://github.com/KhronosGroup/glTF-Tutorials/blob/master/gltfTutorial/gltfTutorial_004_ScenesNodes.md
                            //see the matrix section
                            float[] result = new float[16];
                            nodeModel.computeGlobalTransform(result);
                            layoutBoothEntity.setX(result[12]);
                            layoutBoothEntity.setY(result[13]);
                            layoutBoothEntity.setZ(result[14]);

                            return layoutBoothEntity;
                        })
                        .collect(Collectors.toSet());
        layoutEntity.getBooths().addAll(boothEntities);
        layoutRepository.save(layoutEntity);

    }

    @Override
    public Optional<LayoutDTO> getByJobFairId(String jobFairId) {
        Optional<JobFairEntity> jobFairEntityOpt = jobFairRepository.findById(jobFairId);
        if (!jobFairEntityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }
        JobFairEntity jobFairEntity = jobFairEntityOpt.get();
        return layoutRepository.findById(jobFairEntity.getJobFairBoothList().get(0).getBooth().getLayout().getId()).map(layoutMapper::toDTO);
    }

    @Override
    public Optional<LayoutDTO> getByJobFairIdWithAvailableBoothSlot(String jobFairId) {
        Optional<JobFairEntity> jobFairEntityOpt = jobFairRepository.findById(jobFairId);
        if (!jobFairEntityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobFair.JOB_FAIR_NOT_FOUND));
        }
        JobFairEntity jobFairEntity = jobFairEntityOpt.get();
        Optional<LayoutEntity> layoutOpt = layoutRepository.findById(jobFairEntity.getJobFairBoothList().get(0).getBooth().getLayout().getId());
        if (!layoutOpt.isPresent()) return Optional.empty();
        //check for which booth is available
        LayoutDTO layoutDTO = layoutMapper.toDTO(layoutOpt.get());
        Set<LayoutBoothDTO> newBooths = layoutDTO.getBooths()
                .stream()
                .filter(layoutBoothDTO -> !jobFairBoothRepository.getCompanyBoothByJobFairIdAndBoothId(jobFairId, layoutBoothDTO.getId()).isPresent())
                .collect(Collectors.toSet());
        layoutDTO.setBooths(newBooths);
        return Optional.of(layoutDTO);
    }
}
