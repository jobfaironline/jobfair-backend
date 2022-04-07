package org.capstone.job_fair.services.mappers.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.AssigmentDTO;
import org.capstone.job_fair.models.entities.job_fair.AssignmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class AssignmentMapper {
    public abstract AssigmentDTO toDTO(AssignmentEntity entity);

    public abstract AssignmentEntity toEntity(AssigmentDTO dto);
}
