package org.capstone.job_fair.services.mappers.company.misc;

import org.capstone.job_fair.models.dtos.company.misc.SkillTagDTO;
import org.capstone.job_fair.models.entities.company.misc.SkillTagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class SkillTagMapper {
    public abstract SkillTagDTO toDTO(SkillTagEntity entity);

    public abstract SkillTagEntity toEntity(SkillTagDTO dto);
}
