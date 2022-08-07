package org.capstone.job_fair.services.mappers.payment;


import org.capstone.job_fair.models.dtos.payment.SubscriptionPlanDTO;
import org.capstone.job_fair.models.entities.payment.SubscriptionPlanEntity;
import org.capstone.job_fair.services.mappers.job_fair.ShiftMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class SubscriptionPlanMapper {
    public abstract SubscriptionPlanDTO toDTO(SubscriptionPlanEntity entity);

    public abstract SubscriptionPlanEntity toEntity(SubscriptionPlanDTO dto);

    public abstract void UpdateSubscriptionPlanFromDTO (SubscriptionPlanDTO dto, @MappingTarget SubscriptionPlanEntity entity);
}
