package org.capstone.job_fair.services.mappers.notification;

import org.capstone.job_fair.models.dtos.notification.NotificationMessageDTO;
import org.capstone.job_fair.models.entities.notification.NotificationMessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class NotificationMessageMapper {
    public abstract NotificationMessageDTO toDTO(NotificationMessageEntity entity);

    public abstract NotificationMessageEntity toEntity(NotificationMessageDTO dto);
}
