package org.capstone.job_fair.services.mappers.job_fair;

import org.capstone.job_fair.controllers.payload.requests.job_fair.CreateDecoratedItemMetaDataRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.UpdateDecoratedItemMetaDataRequest;
import org.capstone.job_fair.models.dtos.job_fair.DecoratedItemDTO;
import org.capstone.job_fair.models.entities.job_fair.DecoratedItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class DecoratedItemMapper {
    public abstract DecoratedItemDTO toDTO(DecoratedItemEntity entity);

    public abstract DecoratedItemEntity toEntity(DecoratedItemDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "url", ignore = true)
    public abstract DecoratedItemDTO toDTO(CreateDecoratedItemMetaDataRequest request);

    @Mapping(target = "size", ignore = true)
    @Mapping(target = "url", ignore = true)
    public abstract DecoratedItemDTO toDTO(UpdateDecoratedItemMetaDataRequest request);

    public abstract void updateEntityFromDTO(DecoratedItemDTO dto, @MappingTarget DecoratedItemEntity entity);
}
