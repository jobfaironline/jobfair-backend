package org.capstone.job_fair.services.mappers.notification;

import org.capstone.job_fair.models.dtos.notification.SendToUserDTO;
import org.capstone.job_fair.models.entities.notification.SendToUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class SendToUserMapper {
    public abstract SendToUserDTO toDTO (SendToUserEntity entity);
    public abstract SendToUserEntity toEntity (SendToUserDTO dto);
}
