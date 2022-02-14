package org.capstone.job_fair.services.impl.job_fair;

import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.job_fair.DecoratedItemDTO;
import org.capstone.job_fair.models.entities.job_fair.DecoratedItemEntity;
import org.capstone.job_fair.repositories.job_fair.DecoratedItemRepository;
import org.capstone.job_fair.services.interfaces.job_fair.DecoratedItemService;
import org.capstone.job_fair.services.mappers.DecoratedItemMapper;
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
public class DecoratedItemServiceImpl implements DecoratedItemService {

    @Autowired
    private DecoratedItemRepository decoratedItemRepository;

    @Autowired
    private DecoratedItemMapper decoratedItemMapper;

    @Autowired
    private AwsUtil awsUtil;


    @Override
    public List<DecoratedItemDTO> getAll() {
        return decoratedItemRepository.findAll().stream().map(decoratedItemMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<DecoratedItemDTO> findById(String id) {
        return decoratedItemRepository.findById(id).map(decoratedItemMapper::toDTO);
    }

    @Override
    @Transactional
    public DecoratedItemDTO createNew(DecoratedItemDTO dto) {
        DecoratedItemEntity entity = decoratedItemMapper.toEntity(dto);
        String id = UUID.randomUUID().toString();
        String url = awsUtil.generateAwsS3AccessString(AWSConstant.DECORATED_ITEMS_FOLDER, id);
        entity.setId(id);
        entity.setUrl(url);
        entity = decoratedItemRepository.save(entity);
        return decoratedItemMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public void update(DecoratedItemDTO dto) {
        Optional<DecoratedItemEntity> decoratedItemEntityOpt = decoratedItemRepository.findById(dto.getId());
        if (!decoratedItemEntityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.DecoratedItem.NOT_FOUND));
        }
        DecoratedItemEntity decoratedItemEntity = decoratedItemEntityOpt.get();
        decoratedItemMapper.updateEntityFromDTO(dto, decoratedItemEntity);
        decoratedItemRepository.save(decoratedItemEntity);
    }
}
