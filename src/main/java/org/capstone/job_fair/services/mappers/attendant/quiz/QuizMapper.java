package org.capstone.job_fair.services.mappers.attendant.quiz;

import org.capstone.job_fair.controllers.payload.responses.InProgressQuizResponse;
import org.capstone.job_fair.models.dtos.attendant.test.QuizDTO;
import org.capstone.job_fair.models.dtos.attendant.test.QuizQuestionDTO;
import org.capstone.job_fair.models.entities.attendant.test.QuizEntity;
import org.capstone.job_fair.models.entities.attendant.test.QuizQuestionEntity;
import org.capstone.job_fair.services.mappers.attendant.application.ApplicationMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {QuizChoiceMapper.class, QuizQuestionMapper.class, ApplicationMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class QuizMapper {

    @Autowired
    private QuizQuestionMapper quizQuestionMapper;

    @Mapping(target = "questionList", source = "questionList", qualifiedByName = "toQuestionListEntity")
    public abstract QuizEntity toEntity(QuizDTO dto);

    @Mapping(target = "questionList", source = "questionList", qualifiedByName = "toQuestionListDTO")
    public abstract QuizDTO toDTO(QuizEntity entity);

    @Mapping(target = "applicationId", source = "application.id")
    @Mapping(target = "cvId", source = "application.originCvId")
    @Mapping(target = "boothJobPositionId", source = "application.boothJobPositionDTO.id")
    @Mapping(target = "duration", source = "application.boothJobPositionDTO.testTimeLength")
    @Mapping(target = "jobPositionTitle", source = "application.boothJobPositionDTO.title")
    public abstract InProgressQuizResponse toResponse(QuizDTO dto);

    @Named("toQuestionListEntity")
    public List<QuizQuestionEntity> toQuestionListEntity(List<QuizQuestionDTO> dto) {
        if (dto == null) return null;
        return dto.stream().map(question -> quizQuestionMapper.toEntity(question)).collect(Collectors.toList());
    }

    @Named("toQuestionListDTO")
    public List<QuizQuestionDTO> toQuestionListDTO(List<QuizQuestionEntity> entity) {
        if (entity == null) return null;
        return entity.stream().map(question -> quizQuestionMapper.toDTO(question)).collect(Collectors.toList());
    }


}
