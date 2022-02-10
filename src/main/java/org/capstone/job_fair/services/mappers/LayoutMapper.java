package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.controllers.payload.requests.CreateLayoutMetaDataRequest;
import org.capstone.job_fair.controllers.payload.requests.UpdateLayoutMetaDataRequest;
import org.capstone.job_fair.models.dtos.job_fair.LayoutDTO;
import org.capstone.job_fair.models.entities.job_fair.LayoutEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class LayoutMapper {

    public abstract LayoutEntity toEntity(LayoutDTO dto);

    public abstract LayoutDTO toDTO(LayoutEntity entity);

    public abstract LayoutDTO toDTO(CreateLayoutMetaDataRequest request);

    public abstract LayoutDTO toDTO(UpdateLayoutMetaDataRequest request);

    public abstract void updateEntityFromDTO(LayoutDTO dto, @MappingTarget LayoutEntity entity);
}
