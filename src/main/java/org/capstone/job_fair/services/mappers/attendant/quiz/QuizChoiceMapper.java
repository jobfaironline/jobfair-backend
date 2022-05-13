package org.capstone.job_fair.services.mappers.attendant.quiz;

import org.capstone.job_fair.controllers.payload.responses.InProgressQuizResponse;
import org.capstone.job_fair.models.dtos.attendant.test.QuizChoiceDTO;
import org.capstone.job_fair.models.entities.attendant.test.QuizChoiceEntity;
import org.capstone.job_fair.models.entities.company.job.questions.ChoicesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class QuizChoiceMapper {
    public abstract QuizChoiceDTO toDTO(QuizChoiceEntity entity);

    @Mapping(target = "quizQuestion", ignore = true)
    public abstract QuizChoiceEntity toEntity(QuizChoiceDTO dto);

    @Mapping(target = "quizQuestion", source = "questionId", ignore = true)
    @Mapping(target = "isSelected", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract QuizChoiceEntity toEntity(ChoicesEntity entity);

    public abstract InProgressQuizResponse.QuizChoiceResponse toResponse(QuizChoiceDTO dto);


}
