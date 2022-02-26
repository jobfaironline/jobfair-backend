package org.capstone.job_fair.services.mappers.attendant;

import org.capstone.job_fair.models.dtos.attendant.CountryDTO;
import org.capstone.job_fair.models.entities.attendant.CountryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CountryMapper {

    public abstract CountryDTO toDTO(CountryEntity entity);
}
