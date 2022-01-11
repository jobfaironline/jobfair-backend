package org.capstone.job_fair.models.mappers;

import org.capstone.job_fair.models.dtos.AccountEntityDto;
import org.capstone.job_fair.repositories.attendant.RoleRepository;
import org.hibernate.annotations.Target;
import org.mapstruct.Mapper;
import org.capstone.job_fair.models.entities.AccountEntity;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AccountEntityMapper {

    @Autowired
    protected RoleRepository roleRepository;
    @Mapping(target = "gender", ignore = true)
    public abstract AccountEntityDto toDTO(AccountEntity account);
    @Mapping(target = "gender", ignore = true)
    public abstract AccountEntity toEntity(AccountEntityDto account);
}
