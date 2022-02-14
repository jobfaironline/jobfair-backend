package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.company.MediaDTO;
import org.capstone.job_fair.models.entities.company.MediaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class MediaEntityMapper {
    public abstract MediaEntity toEntity(MediaDTO dto);

    public abstract MediaDTO toDTO(MediaEntity entity);
}
