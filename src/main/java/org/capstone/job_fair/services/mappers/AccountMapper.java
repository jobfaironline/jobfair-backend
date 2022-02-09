package org.capstone.job_fair.services.mappers;

import org.capstone.job_fair.controllers.payload.requests.StaffRegisterRequest;
import org.capstone.job_fair.controllers.payload.requests.UpdateAttendantRequest;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.account.GenderEntity;
import org.capstone.job_fair.models.entities.account.RoleEntity;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.models.enums.Role;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class AccountMapper {
    @Mapping(target = "gender", qualifiedByName = "toAccountDTOGender")
    @Mapping(target = "role", qualifiedByName = "toAccountDTORole")
    public abstract AccountDTO toDTO(AccountEntity account);

    @Mapping(target = "gender", qualifiedByName = "toAccountEntityGender")
    @Mapping(target = "role", qualifiedByName = "toAccountEntityRole")
    public abstract AccountEntity toEntity(AccountDTO account);

    public abstract AccountDTO toDTO(UpdateAttendantRequest.AccountRequest request);

    public abstract AccountDTO toDTO(StaffRegisterRequest request);

    @Named("toAccountDTOGender")
    public static Gender toAccountDTOGender(GenderEntity gender) {
        if (gender == null) return null;
        return Gender.values()[gender.getId()];
    }

    @Named("toAccountEntityGender")
    public static GenderEntity toAccountEntityGender(Gender gender) {
        if (gender == null) return null;
        GenderEntity genderEntity = new GenderEntity();
        genderEntity.setId(gender.ordinal());
        return genderEntity;
    }

    @Named("toAccountEntityRole")
    public static RoleEntity toAccountEntityRole(Role role) {
        if (role == null) return null;
        RoleEntity entity = new RoleEntity();
        entity.setId(role.ordinal());
        entity.setName(role.name());
        return entity;
    }

    @Named("toAccountDTORole")
    public Role roleEntityToRole(RoleEntity entity) {
        if (entity == null) return null;
        return Role.values()[entity.getId()];
    }

    public abstract void updateAccountMapperFromDto(AccountDTO dto, @MappingTarget AccountEntity entity);
}