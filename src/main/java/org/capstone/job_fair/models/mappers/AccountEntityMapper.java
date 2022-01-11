package org.capstone.job_fair.models.mappers;

import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.repositories.attendant.RoleRepository;
import org.mapstruct.Mapper;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AccountEntityMapper {

    @Autowired
    protected RoleRepository roleRepository;
    @Mapping(target = "gender", ignore = true)
    public abstract AccountDTO toDTO(AccountEntity account);
    @Mapping(target = "gender", ignore = true)
    public abstract AccountEntity toEntity(AccountDTO account);
}
