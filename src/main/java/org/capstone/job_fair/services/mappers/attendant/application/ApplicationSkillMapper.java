package org.capstone.job_fair.services.mappers.attendant.application;

import org.capstone.job_fair.models.dtos.attendant.application.ApplicationSkillDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationSkillEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CvSkillEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ApplicationSkillMapper {
    public abstract ApplicationSkillDTO toDTO(ApplicationSkillEntity entity);

    @Mapping(target = "application", ignore = true)
    public abstract ApplicationSkillEntity toEntity(ApplicationSkillDTO dto);

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "application", ignore = true)
    public abstract ApplicationSkillEntity toEntity(CvSkillEntity cvSkill);
}
