package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.token.AccountVerifyTokenDTO;
import org.capstone.job_fair.models.entities.token.AccountVerifyTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountVerifyTokenMapper {
    public AccountVerifyTokenDTO toAccountVerifyTokenDto(AccountVerifyTokenEntity entity);
    public AccountVerifyTokenEntity toAccountVerifyTokenEntity(AccountVerifyTokenDTO dto);
}
