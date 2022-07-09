package org.capstone.job_fair.services.mappers.attendant.cv;

import org.capstone.job_fair.controllers.payload.requests.attendant.cv.DraftCvRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.CvSkillDTO;
import org.capstone.job_fair.models.dtos.attendant.profile.SkillDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvSkillEntity;
import org.capstone.job_fair.models.entities.attendant.profile.SkillEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CvSkillMapper {
    public abstract CvSkillDTO toDTO(CvSkillEntity entity);

    @Mapping(target = "cv", ignore = true)
    public abstract CvSkillEntity toEntity(CvSkillDTO dto);

    @Mapping(target = "id", ignore = true)
    public abstract CvSkillDTO toDTO(DraftCvRequest.Skills request);

    public abstract void updateCvSkillEntityFromCvSkillDTO(CvSkillDTO dto, @MappingTarget CvSkillEntity entity);

}
