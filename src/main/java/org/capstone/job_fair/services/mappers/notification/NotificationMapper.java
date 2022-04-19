package org.capstone.job_fair.services.mappers.notification;


import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.dtos.notification.NotificationDTO;
import org.capstone.job_fair.models.entities.notification.NotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class NotificationMapper {

    public abstract NotificationEntity toEntity(NotificationDTO dto);

    public abstract NotificationDTO toDTO(NotificationEntity entity);
}
