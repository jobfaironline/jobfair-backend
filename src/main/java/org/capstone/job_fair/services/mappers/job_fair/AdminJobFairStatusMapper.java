package org.capstone.job_fair.services.mappers.job_fair;


import org.capstone.job_fair.models.dtos.job_fair.AdminJobFairStatusDTO;
import org.capstone.job_fair.models.entities.job_fair.AdminJobFairStatusEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {JobFairMapper.class}
)
public abstract class AdminJobFairStatusMapper {
    public abstract AdminJobFairStatusDTO toDTO(AdminJobFairStatusEntity entity);
    public abstract AdminJobFairStatusEntity toEntity(AdminJobFairStatusDTO dto);
}
