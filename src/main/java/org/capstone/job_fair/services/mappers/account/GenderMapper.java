package org.capstone.job_fair.services.mappers.account;

import org.capstone.job_fair.models.dtos.account.GenderDTO;
import org.capstone.job_fair.models.entities.account.GenderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class GenderMapper {
    public abstract GenderDTO toDTO(GenderEntity entity);

    public abstract GenderEntity toEntity(GenderDTO dto);
}
