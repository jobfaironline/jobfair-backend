package org.capstone.job_fair.services.mappers.account;

import org.capstone.job_fair.models.dtos.account.RoleDTO;
import org.capstone.job_fair.models.entities.account.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class RoleMapper {
    public abstract RoleDTO toDTO(RoleEntity entity);

    public abstract RoleEntity toEntity(RoleDTO dto);
}
