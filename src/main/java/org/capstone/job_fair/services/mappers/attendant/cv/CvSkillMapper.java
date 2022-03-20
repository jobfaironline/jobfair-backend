package org.capstone.job_fair.services.mappers.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.CvSkillDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvSkillEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CvSkillMapper {
    public abstract CvSkillDTO toDTO(CvSkillEntity entity);
    public abstract CvSkillEntity toEntity(CvSkillDTO dto);
}
