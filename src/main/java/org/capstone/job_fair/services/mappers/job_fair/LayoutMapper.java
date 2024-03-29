package org.capstone.job_fair.services.mappers.job_fair;

import org.capstone.job_fair.controllers.payload.requests.job_fair.CreateLayoutMetaDataRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.UpdateLayoutMetaDataRequest;
import org.capstone.job_fair.models.dtos.job_fair.LayoutDTO;
import org.capstone.job_fair.models.entities.job_fair.LayoutEntity;
import org.capstone.job_fair.services.mappers.company.CompanyMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {LayoutBoothMapper.class, CompanyMapper.class})
public abstract class LayoutMapper {

    public abstract LayoutEntity toEntity(LayoutDTO dto);

    public abstract LayoutDTO toDTO(LayoutEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "url", ignore = true)
    @Mapping(target = "booths", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "thumbnailUrl", ignore = true)
    public abstract LayoutDTO toDTO(CreateLayoutMetaDataRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "url", ignore = true)
    @Mapping(target = "booths", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "thumbnailUrl", ignore = true)
    public abstract LayoutDTO toDTO(UpdateLayoutMetaDataRequest request);

    @Mapping(target = "company", source = "company", ignore = true)
    public abstract void updateEntityFromDTO(LayoutDTO dto, @MappingTarget LayoutEntity entity);
}
