package org.capstone.job_fair.services.mappers.attendant.misc;

import org.capstone.job_fair.models.dtos.attendant.misc.CountryDTO;
import org.capstone.job_fair.models.entities.attendant.misc.CountryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CountryMapper {

    public abstract CountryDTO toDTO(CountryEntity entity);

    public abstract CountryEntity toEntity(CountryDTO dto);
}
