package org.capstone.job_fair.services.impl.job_fair;

import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.job_fair.DecoratedItemDTO;
import org.capstone.job_fair.models.entities.job_fair.DecoratedItemEntity;
import org.capstone.job_fair.repositories.job_fair.DecoratedItemRepository;
import org.capstone.job_fair.services.interfaces.job_fair.DecoratedItemService;
import org.capstone.job_fair.services.mappers.DecoratedItemMapper;
import org.capstone.job_fair.utils.DomainUtil;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DecoratedItemServiceImpl implements DecoratedItemService {

    @Value("${amazonProperties.cdn-link}")
    private String cdnLink;

    @Autowired
    private DecoratedItemRepository decoratedItemRepository;

    @Autowired
    private DecoratedItemMapper decoratedItemMapper;

    @Autowired
    private DomainUtil domainUtil;

    @Override
    public List<DecoratedItemDTO> getAll() {
        return decoratedItemRepository.findAll().stream().map(decoratedItemMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<DecoratedItemDTO> findById(String id) {
        return decoratedItemRepository.findById(id).map(decoratedItemMapper::toDTO);
    }

    @Override
    public DecoratedItemDTO createNew(DecoratedItemDTO dto) {
        DecoratedItemEntity entity = decoratedItemMapper.toEntity(dto);
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        StringBuffer url = new StringBuffer(cdnLink);
        url.append("/");
        url.append(AWSConstant.IMAGE_FOLDER);
        url.append("/");
        url.append(entity.getId());
        entity.setUrl(url.toString());
        entity = decoratedItemRepository.save(entity);
        return decoratedItemMapper.toDTO(entity);
    }

    @Override
    public void update(DecoratedItemDTO dto) {
        Optional<DecoratedItemEntity> decoratedItemEntityOpt = decoratedItemRepository.findById(dto.getId());
        if (!decoratedItemEntityOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND));
        }
        DecoratedItemEntity decoratedItemEntity = decoratedItemEntityOpt.get();
        decoratedItemMapper.updateEntityFromDTO(dto, decoratedItemEntity);
    }
}
