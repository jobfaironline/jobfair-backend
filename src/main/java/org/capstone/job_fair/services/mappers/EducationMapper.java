package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.controllers.payload.requests.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.EducationDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.SkillDTO;
import org.capstone.job_fair.models.entities.attendant.cv.EducationEntity;
import org.capstone.job_fair.models.entities.attendant.cv.SkillEntity;
import org.capstone.job_fair.models.enums.Qualification;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class EducationMapper {
    public abstract EducationEntity toEntity(EducationDTO dto);
    public abstract EducationDTO toDTO(EducationEntity entity);

    public abstract EducationDTO toDTO(UpdateAttendantRequest.Educations request);

    public abstract void updateEducationEntityFromEducationDTO(EducationDTO dto, @MappingTarget EducationEntity entity);

}
