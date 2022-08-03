package org.capstone.job_fair.services.mappers.payment;


import org.capstone.job_fair.controllers.payload.responses.SubscriptionResponse;
import org.capstone.job_fair.models.dtos.payment.SubscriptionDTO;
import org.capstone.job_fair.models.entities.payment.SubscriptionEntity;
import org.capstone.job_fair.services.mappers.company.CompanyMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {CompanyMapper.class, SubscriptionPlanMapper.class})
public abstract class SubscriptionMapper {

    public abstract SubscriptionDTO toDTO(SubscriptionEntity entity);

    public abstract SubscriptionEntity toEntity(SubscriptionDTO dto);

    @Mapping(target = "companyName", source = "company.name")
    public abstract SubscriptionResponse toResponse(SubscriptionDTO dto);
}
