package org.capstone.job_fair.services.mappers.company.question;

import org.capstone.job_fair.controllers.payload.requests.company.CreateQuestionsRequest;
import org.capstone.job_fair.models.dtos.company.job.questions.ChoicesDTO;
import org.capstone.job_fair.models.dtos.company.job.questions.QuestionsDTO;
import org.capstone.job_fair.models.entities.company.job.questions.ChoicesEntity;
import org.capstone.job_fair.models.entities.company.job.questions.QuestionsEntity;
import org.capstone.job_fair.services.mappers.company.JobPositionMapper;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {JobPositionMapper.class, ChoicesMapper.class})
public abstract class QuestionsMapper {

    @Autowired
    private ChoicesMapper mapper;

    @Mapping(target = "choicesList", source = "choicesList", qualifiedByName = "toChoicesListEntity")
    public abstract QuestionsEntity toEntity(QuestionsDTO dto);

    @Mapping(target = "choicesList", source = "choicesList", qualifiedByName = "toChoicesListDTO")
    public abstract QuestionsDTO toDTO(QuestionsEntity entity);

    @Mapping(target = "choicesList", source = "choicesList", qualifiedByName = "requestToDTO")
    public abstract QuestionsDTO toDTO(CreateQuestionsRequest request);

    public abstract void updateQuestion(QuestionsDTO dto, @MappingTarget QuestionsEntity entity);


    @Named("requestToDTO")
    public List<ChoicesDTO> fromChoiceInCreateQuestionsRequest(List<CreateQuestionsRequest.Choice> choiceList) {
        if (choiceList == null) return null;
        return choiceList.stream().map(choice -> mapper.toChoicesDTO(choice)).collect(Collectors.toList());
    }

    @Named("toChoicesListDTO")
    public List<ChoicesDTO> toChoicesListDTO(List<ChoicesEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream().map(entity -> mapper.toChoicesDTO(entity)).collect(Collectors.toList());
    }

    @Named("toChoicesListEntity")
    public List<ChoicesEntity> toChoicesListEntity(List<ChoicesDTO> dtoList) {
        if (dtoList == null) return null;
        return dtoList.stream().map(dto -> mapper.toChoicesEntity(dto)).collect(Collectors.toList());
    }

}
