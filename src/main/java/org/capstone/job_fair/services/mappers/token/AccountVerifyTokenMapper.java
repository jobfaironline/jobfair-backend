package org.capstone.job_fair.services.mappers.token;

import org.capstone.job_fair.models.dtos.token.AccountVerifyTokenDTO;
import org.capstone.job_fair.models.entities.token.AccountVerifyTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class AccountVerifyTokenMapper {
    public abstract AccountVerifyTokenDTO toAccountVerifyTokenDto(AccountVerifyTokenEntity entity);

    public abstract AccountVerifyTokenEntity toAccountVerifyTokenEntity(AccountVerifyTokenDTO dto);
}
