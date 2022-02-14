package org.capstone.job_fair.services.mappers.company;

import org.capstone.job_fair.models.dtos.company.MediaDTO;
import org.capstone.job_fair.models.entities.company.MediaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CompanyMapper.class}
)
public abstract class MediaEntityMapper {

    @Mapping(source = "companyDTO", target = "company")

    public abstract MediaEntity toEntity(MediaDTO dto);

    @Mapping(source = "company", target = "companyDTO")
    public abstract MediaDTO toDTO(MediaEntity entity);
}
