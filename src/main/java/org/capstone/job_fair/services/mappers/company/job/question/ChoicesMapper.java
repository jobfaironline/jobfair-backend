package org.capstone.job_fair.services.mappers.company.job.question;

import org.capstone.job_fair.controllers.payload.requests.company.CreateQuestionsRequest;
import org.capstone.job_fair.models.dtos.company.job.questions.ChoicesDTO;
import org.capstone.job_fair.models.entities.company.job.questions.ChoicesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {QuestionsMapper.class})
public abstract class ChoicesMapper {
    public abstract ChoicesDTO toChoicesDTO(ChoicesEntity entity);

    public abstract ChoicesEntity toChoicesEntity(ChoicesDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "questionId", ignore = true)
    public abstract ChoicesDTO toChoicesDTO(CreateQuestionsRequest.Choice choice);
}
