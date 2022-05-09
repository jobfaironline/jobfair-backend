package org.capstone.job_fair.services.mappers.attendant.quiz;

import org.capstone.job_fair.controllers.payload.responses.InProgressQuizResponse;
import org.capstone.job_fair.models.dtos.attendant.test.QuizChoiceDTO;
import org.capstone.job_fair.models.dtos.attendant.test.QuizQuestionDTO;
import org.capstone.job_fair.models.entities.attendant.test.QuizChoiceEntity;
import org.capstone.job_fair.models.entities.attendant.test.QuizQuestionEntity;
import org.capstone.job_fair.models.entities.company.job.questions.ChoicesEntity;
import org.capstone.job_fair.models.entities.company.job.questions.QuestionsEntity;
import org.capstone.job_fair.models.enums.QuizQuestionType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {QuizChoiceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public abstract class QuizQuestionMapper {

    @Autowired
    private QuizChoiceMapper quizChoiceMapper;

    @Mapping(target = "choiceList", source = "choiceList", qualifiedByName = "toChoiceListDTO")
    public abstract QuizQuestionDTO toDTO(QuizQuestionEntity entity);

    @Mapping(target = "choiceList", source = "choiceList", qualifiedByName = "toChoiceListEntityFromQuizChoiceDTO")
    public abstract QuizQuestionEntity toEntity(QuizQuestionDTO dto);

    @Mapping(source = "id", target = "id", ignore = true)
    @Mapping(source = "choicesList", target = "choiceList", qualifiedByName = "toChoiceListEntityFromChoicesEntity")
    @Mapping(target = "quizId", ignore = true)
    public abstract QuizQuestionEntity toEntity(QuestionsEntity entity);

    public InProgressQuizResponse.QuizQuestionResponse toResponse(QuizQuestionDTO dto) {
        InProgressQuizResponse.QuizQuestionResponse response = new InProgressQuizResponse.QuizQuestionResponse();
        response.setContent(dto.getContent());
        response.setId(dto.getId());
        response.setChoiceList(dto.getChoiceList().stream().map(quizChoiceMapper::toResponse).collect(Collectors.toList()));
        if (dto.getChoiceList().stream().filter(QuizChoiceDTO::getIsCorrect).count() > 1) {
            response.setType(QuizQuestionType.MULTIPLE_CHOICE);
        } else {
            response.setType(QuizQuestionType.ONE_CHOICE);
        }
        return response;
    }


    @Named("toChoiceListDTO")
    public List<QuizChoiceDTO> toChoiceListDTO(List<QuizChoiceEntity> entity) {
        if (entity == null) return null;
        return entity.stream().map(choice -> quizChoiceMapper.toDTO(choice)).collect(Collectors.toList());
    }

    @Named("toChoiceListEntityFromQuizChoiceDTO")
    public List<QuizChoiceEntity> toChoiceListEntityFromQuizChoiceDTO(List<QuizChoiceDTO> dto) {
        if (dto == null) return null;
        return dto.stream().map(choice -> quizChoiceMapper.toEntity(choice)).collect(Collectors.toList());
    }

    @Named("toChoiceListEntityFromChoicesEntity")
    public List<QuizChoiceEntity> toChoiceListEntityFromChoicesEntity(List<ChoicesEntity> entity) {
        if (entity == null) return null;
        return entity.stream().map(choice -> quizChoiceMapper.toEntity(choice)).collect(Collectors.toList());
    }

}
