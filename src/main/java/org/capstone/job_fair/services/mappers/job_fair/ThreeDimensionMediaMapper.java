package org.capstone.job_fair.services.mappers.job_fair;

import org.capstone.job_fair.controllers.payload.requests.job_fair.CreateDecoratedItemMetaDataRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.UpdateDecoratedItemMetaDataRequest;
import org.capstone.job_fair.models.dtos.job_fair.ThreeDimensionMediaDTO;
import org.capstone.job_fair.models.entities.job_fair.ThreeDimensionMedia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ThreeDimensionMediaMapper {
    public abstract ThreeDimensionMediaDTO toDTO(ThreeDimensionMedia entity);

    public abstract ThreeDimensionMedia toEntity(ThreeDimensionMediaDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "url", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "thumbnailUrl", ignore = true)
    public abstract ThreeDimensionMediaDTO toDTO(CreateDecoratedItemMetaDataRequest request);

    @Mapping(target = "url", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "thumbnailUrl", ignore = true)
    public abstract ThreeDimensionMediaDTO toDTO(UpdateDecoratedItemMetaDataRequest request);

    public abstract void updateEntityFromDTO(ThreeDimensionMediaDTO dto, @MappingTarget ThreeDimensionMedia entity);
}
