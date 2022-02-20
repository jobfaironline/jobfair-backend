package org.capstone.job_fair.services.mappers.company;


import org.capstone.job_fair.controllers.payload.requests.company.CreateCompanyBoothLayoutMetaDataRequest;
import org.capstone.job_fair.models.dtos.company.CompanyBoothLayoutDTO;
import org.capstone.job_fair.models.entities.company.CompanyBoothLayoutEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CompanyBoothMapper.class}
)
public abstract class CompanyBoothLayoutMapper {
    public abstract CompanyBoothLayoutEntity toEntity(CompanyBoothLayoutDTO dto);

    public abstract CompanyBoothLayoutDTO toDTO(CompanyBoothLayoutEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "companyBooth.id", source = "companyBoothId")
    @Mapping(target = "url", ignore = true)
    public abstract CompanyBoothLayoutDTO toDTO(CreateCompanyBoothLayoutMetaDataRequest request);
}
