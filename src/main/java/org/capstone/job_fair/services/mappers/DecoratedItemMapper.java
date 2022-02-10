package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.controllers.payload.requests.CreateDecoratedItemMetaDataRequest;
import org.capstone.job_fair.controllers.payload.requests.UpdateDecoratedItemMetaDataRequest;
import org.capstone.job_fair.models.dtos.job_fair.DecoratedItemDTO;
import org.capstone.job_fair.models.entities.job_fair.DecoratedItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class DecoratedItemMapper {
    public abstract DecoratedItemDTO toDTO(DecoratedItemEntity entity);

    public abstract DecoratedItemEntity toEntity(DecoratedItemDTO dto);

    public abstract DecoratedItemDTO toDTO(CreateDecoratedItemMetaDataRequest request);

    public abstract DecoratedItemDTO toDTO(UpdateDecoratedItemMetaDataRequest request);

    public abstract void updateEntityFromDTO(DecoratedItemDTO dto, @MappingTarget DecoratedItemEntity entity);
}
