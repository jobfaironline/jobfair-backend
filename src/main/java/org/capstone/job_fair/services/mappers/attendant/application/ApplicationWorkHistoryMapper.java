package org.capstone.job_fair.services.mappers.attendant.application;

import org.capstone.job_fair.models.dtos.attendant.application.ApplicationWorkHistoryDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationWorkHistoryEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CvWorkHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ApplicationWorkHistoryMapper {
    public abstract ApplicationWorkHistoryEntity toEntity(ApplicationWorkHistoryDTO dto);

    public abstract ApplicationWorkHistoryDTO toDTO(ApplicationWorkHistoryEntity entity);

    @Mapping(target = "id", source = "id", ignore = true)
    public abstract ApplicationWorkHistoryEntity toEntity(CvWorkHistoryEntity cvWorkHistory);
}
