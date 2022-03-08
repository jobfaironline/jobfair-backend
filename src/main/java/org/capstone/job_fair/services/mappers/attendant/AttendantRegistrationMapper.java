package org.capstone.job_fair.services.mappers.attendant;


import org.capstone.job_fair.models.dtos.attendant.AttendantRegistrationDTO;
import org.capstone.job_fair.models.entities.attendant.AttendantRegistrationEntity;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public abstract class AttendantRegistrationMapper {

    public abstract AttendantRegistrationDTO toDTO(AttendantRegistrationEntity entity);

    public abstract AttendantRegistrationEntity toEntity(AttendantRegistrationDTO dto);
}