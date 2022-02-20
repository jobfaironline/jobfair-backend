package org.capstone.job_fair.services.impl.job_fair;

import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.job_fair.ThreeDimensionMediaDTO;
import org.capstone.job_fair.models.entities.job_fair.ThreeDimensionMedia;
import org.capstone.job_fair.repositories.job_fair.DecoratedItemRepository;
import org.capstone.job_fair.services.interfaces.job_fair.ThreeDimensionMediaService;
import org.capstone.job_fair.services.mappers.job_fair.ThreeDimensionMediaMapper;
import org.capstone.job_fair.utils.AwsUtil;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ThreeDimensionMediaServiceImpl implements ThreeDimensionMediaService {

    @Autowired
    private DecoratedItemRepository decoratedItemRepository;

    @Autowired
    private ThreeDimensionMediaMapper threeDimensionMediaMapper;

    @Autowired
    private AwsUtil awsUtil;


    @Override
    public List<ThreeDimensionMediaDTO> getAll() {
        return decoratedItemRepository.findAll().stream().map(threeDimensionMediaMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<ThreeDimensionMediaDTO> findById(String id) {
        return decoratedItemRepository.findById(id).map(threeDimensionMediaMapper::toDTO);
    }

    @Override
    @Transactional
    public ThreeDimensionMediaDTO createNew(ThreeDimensionMediaDTO dto) {
        ThreeDimensionMedia entity = threeDimensionMediaMapper.toEntity(dto);
        String id = UUID.randomUUID().toString();
        String url = awsUtil.generateAwsS3AccessString(AWSConstant.DECORATED_ITEMS_FOLDER, id);
        entity.setId(id);
        entity.setUrl(url);
        entity = decoratedItemRepository.save(entity);
        return threeDimensionMediaMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public void update(ThreeDimensionMediaDTO dto) {
        Optional<ThreeDimensionMedia> decoratedItemEntityOpt = decoratedItemRepository.findById(dto.getId());
        if (!decoratedItemEntityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.DecoratedItem.NOT_FOUND));
        }
        ThreeDimensionMedia threeDimensionMedia = decoratedItemEntityOpt.get();
        threeDimensionMediaMapper.updateEntityFromDTO(dto, threeDimensionMedia);
        decoratedItemRepository.save(threeDimensionMedia);
    }
}
