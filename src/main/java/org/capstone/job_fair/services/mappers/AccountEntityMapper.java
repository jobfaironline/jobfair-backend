package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.repositories.account.RoleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public abstract class AccountEntityMapper {

    @Mapping(target = "gender", ignore = true)
    public abstract AccountDTO toDTO(AccountEntity account);
    @Mapping(target = "gender", ignore = true)
    public abstract AccountEntity toEntity(AccountDTO account);
    public abstract void updateAccountMapperFromDto(AccountDTO dto, @MappingTarget AccountEntity entity);
}
