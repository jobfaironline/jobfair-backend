package org.capstone.job_fair.services.mappers.company;

import org.capstone.job_fair.controllers.payload.requests.company.CompanyEmployeeRegisterRequest;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.services.mappers.account.AccountMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {CompanyMapper.class, AccountMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CompanyEmployeeMapper {
    @Mapping(source = "company", target = "companyDTO")
    public abstract CompanyEmployeeDTO toDTO(CompanyEmployeeEntity attendant);

    @Mapping(source = "companyDTO", target = "company")
    public abstract CompanyEmployeeEntity toEntity(CompanyEmployeeDTO dto);

    @Mapping(source = "companyDTO", target = "company")
    public abstract void updateCompanyEmployeeMapperFromDto(CompanyEmployeeDTO dto, @MappingTarget CompanyEmployeeEntity entity);

    @Mapping(source = "email", target = "account.email")
    @Mapping(source = "firstName", target = "account.firstname")
    @Mapping(source = "middleName", target = "account.middlename")
    @Mapping(source = "lastName", target = "account.lastname")
    @Mapping(source = "phone", target = "account.phone")
    @Mapping(source = "gender", target = "account.gender")
    @Mapping(target = "accountId", ignore = true)
    public abstract CompanyEmployeeDTO toDTO(CompanyEmployeeRegisterRequest request);
}
