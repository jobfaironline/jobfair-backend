package org.capstone.job_fair.services.mappers.dynamoDB;

import org.capstone.job_fair.models.dtos.dynamoDB.NotificationMessageDTO;
import org.capstone.job_fair.models.entities.dynamoDB.NotificationMessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class NotificationMessageMapper {
    public abstract NotificationMessageDTO toDTO(NotificationMessageEntity entity);

    public abstract NotificationMessageEntity toEntity(NotificationMessageDTO dto);
}
