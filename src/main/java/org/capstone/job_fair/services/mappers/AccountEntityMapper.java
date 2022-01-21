package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.account.GenderDTO;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.entities.account.GenderEntity;
import org.capstone.job_fair.models.entities.account.RoleEntity;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.repositories.account.RoleRepository;
import org.mapstruct.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class AccountEntityMapper {
    @Mapping(target = "gender", qualifiedByName = "toAccountDTOGender")
    @Mapping(target = "role", qualifiedByName = "toAccountDTORole")
    public abstract AccountDTO toDTO(AccountEntity account);

    @Mapping(target = "gender", qualifiedByName = "toAccountEntityGender")
    @Mapping(target = "role", qualifiedByName = "toAccountEntityRole")
    public abstract AccountEntity toEntity(AccountDTO account);

    @Named("toAccountDTOGender")
    public static Gender toAccountDTOGender(GenderEntity gender) {
        return Gender.values()[gender.getId()];
    }

    @Named("toAccountEntityGender")
    public static GenderEntity toAccountEntityGender(Gender gender) {
        GenderEntity genderEntity = new GenderEntity();
        genderEntity.setId(gender.ordinal());
        return genderEntity;
    }

    @Named("toAccountEntityRole")
    public static RoleEntity toAccountEntityRole(Role role) {
        RoleEntity entity = new RoleEntity();
        entity.setId(role.ordinal());
        entity.setName(role.name());
        return entity;
    }

    @Named("toAccountDTORole")
    public Role roleEntityToRole(RoleEntity entity) {
        return Role.values()[entity.getId()];
    }

    public abstract void updateAccountMapperFromDto(AccountDTO dto, @MappingTarget AccountEntity entity);
}
