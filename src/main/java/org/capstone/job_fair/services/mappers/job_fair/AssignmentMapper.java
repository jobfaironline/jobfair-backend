package org.capstone.job_fair.services.mappers.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.AssignmentDTO;
import org.capstone.job_fair.models.entities.job_fair.AssignmentEntity;
import org.capstone.job_fair.services.mappers.company.CompanyEmployeeMapper;
import org.capstone.job_fair.services.mappers.company.JobFairBoothMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CompanyEmployeeMapper.class, JobFairBoothMapper.class}
)
public abstract class AssignmentMapper {
    public abstract AssignmentDTO toDTO(AssignmentEntity entity);

    public abstract AssignmentEntity toEntity(AssignmentDTO dto);
}
