package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.repositories.account.RoleRepository;
import org.mapstruct.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
public abstract class AccountEntityMapper {
    @Mapping(target = "gender", ignore = true)
    public abstract AccountDTO toDTO(AccountEntity account);
    @Mapping(target = "gender", ignore = true)
    public abstract AccountEntity toEntity(AccountDTO account);

    /*@Named("toDTOGender")
    public static double toDTOGender(int inch) {
        return inch * 2.54;
    }*/

    public abstract void updateAccountMapperFromDto(AccountDTO dto, @MappingTarget AccountEntity entity);
}
