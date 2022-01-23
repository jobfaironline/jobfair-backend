package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.controllers.payload.requests.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.SkillDTO;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.entities.attendant.cv.SkillEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class SkillMapper {
    public abstract SkillEntity toEntity(SkillDTO dto);
    public abstract SkillDTO toDTO(SkillEntity entity);

    public abstract SkillDTO toDTO(UpdateAttendantRequest.Skills request);

    public abstract void updateSkillEntityFromSkillDTO(SkillDTO dto, @MappingTarget SkillEntity entity);
}
