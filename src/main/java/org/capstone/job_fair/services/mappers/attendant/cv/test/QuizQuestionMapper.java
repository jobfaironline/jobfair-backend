package org.capstone.job_fair.services.mappers.attendant.cv.test;

import org.capstone.job_fair.models.dtos.attendant.cv.test.QuizChoiceDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.test.QuizQuestionDTO;
import org.capstone.job_fair.models.entities.attendant.cv.test.QuizChoiceEntity;
import org.capstone.job_fair.models.entities.attendant.cv.test.QuizQuestionEntity;
import org.capstone.job_fair.models.entities.company.job.questions.ChoicesEntity;
import org.capstone.job_fair.models.entities.company.job.questions.QuestionsEntity;
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

    @Mapping(target = "choiceList" , source = "choiceList", qualifiedByName = "toChoiceListDTO")
    public abstract QuizQuestionDTO toDTO (QuizQuestionEntity entity);

    @Mapping(target = "choiceList" , source = "choiceList", qualifiedByName = "toChoiceListEntityFromQuizChoiceDTO")
    public abstract QuizQuestionEntity toEntity (QuizQuestionDTO dto);

    @Mapping(source = "id", target = "id", ignore = true)
    @Mapping(source = "choicesList", target = "choiceList", qualifiedByName = "toChoiceListEntityFromChoicesEntity")
    @Mapping(target = "quizId", ignore = true)
    public abstract QuizQuestionEntity toEntity (QuestionsEntity entity);


    @Named("toChoiceListDTO")
    public List<QuizChoiceDTO> toChoiceListDTO (List<QuizChoiceEntity> entity){
        if (entity == null) return null;
        return entity.stream().map(choice -> quizChoiceMapper.toDTO(choice)).collect(Collectors.toList());
    }

    @Named("toChoiceListEntityFromQuizChoiceDTO")
    public List<QuizChoiceEntity> toChoiceListEntityFromQuizChoiceDTO (List<QuizChoiceDTO> dto){
        if (dto == null) return null;
        return dto.stream().map(choice -> quizChoiceMapper.toEntity(choice)).collect(Collectors.toList());
    }

    @Named("toChoiceListEntityFromChoicesEntity")
    public List<QuizChoiceEntity> toChoiceListEntityFromChoicesEntity (List<ChoicesEntity> entity){
        if (entity == null) return null;
        return entity.stream().map(choice -> quizChoiceMapper.toEntity(choice)).collect(Collectors.toList());
    }

}
