package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.controllers.payload.requests.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.cv.SkillDTO;
import org.capstone.job_fair.models.entities.attendant.cv.SkillEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class SkillEntityMapper {
    public abstract SkillEntity toEntity(SkillDTO dto);

    public abstract SkillDTO toDTO(UpdateAttendantRequest.SkillRequest request);
}
