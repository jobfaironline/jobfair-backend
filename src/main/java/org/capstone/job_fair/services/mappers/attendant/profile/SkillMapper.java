package org.capstone.job_fair.services.mappers.attendant.profile;

import org.capstone.job_fair.controllers.payload.requests.attendant.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.attendant.profile.SkillDTO;
import org.capstone.job_fair.models.entities.attendant.profile.SkillEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class SkillMapper {
    public abstract SkillEntity toEntity(SkillDTO dto);

    public abstract SkillDTO toDTO(SkillEntity entity);

    public abstract SkillDTO toDTO(UpdateAttendantRequest.Skills request);

    public abstract void updateSkillEntityFromSkillDTO(SkillDTO dto, @MappingTarget SkillEntity entity);
}
