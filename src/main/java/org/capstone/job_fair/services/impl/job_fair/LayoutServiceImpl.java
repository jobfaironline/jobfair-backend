package org.capstone.job_fair.services.impl.job_fair;

import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.job_fair.LayoutDTO;
import org.capstone.job_fair.models.entities.job_fair.LayoutEntity;
import org.capstone.job_fair.repositories.job_fair.LayoutRepository;
import org.capstone.job_fair.services.interfaces.job_fair.LayoutService;
import org.capstone.job_fair.services.mappers.LayoutMapper;
import org.capstone.job_fair.utils.AwsUtil;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LayoutServiceImpl implements LayoutService {

    @Autowired
    private LayoutRepository layoutRepository;

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
    public void update(LayoutDTO dto) {
        Optional<LayoutEntity> layoutEntityOpt = layoutRepository.findById(dto.getId());
        if (!layoutEntityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND));
        }
        LayoutEntity layoutEntity = layoutEntityOpt.get();
        layoutMapper.updateEntityFromDTO(dto, layoutEntity);
        layoutRepository.save(layoutEntity);
    }
}
