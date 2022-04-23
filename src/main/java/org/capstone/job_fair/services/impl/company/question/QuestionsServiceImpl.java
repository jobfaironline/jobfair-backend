package org.capstone.job_fair.services.impl.company.question;

import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.constants.QuestionConstant;
import org.capstone.job_fair.models.dtos.company.job.questions.QuestionsDTO;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.capstone.job_fair.models.entities.company.job.questions.QuestionsEntity;
import org.capstone.job_fair.models.statuses.QuestionStatus;
import org.capstone.job_fair.repositories.company.job.JobPositionRepository;
import org.capstone.job_fair.repositories.company.job.questions.QuestionsRepository;
import org.capstone.job_fair.services.interfaces.company.question.QuestionsService;
import org.capstone.job_fair.services.mappers.company.question.QuestionsMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionsServiceImpl implements QuestionsService {

    @Autowired
    private JobPositionRepository jobPositionRepository;

    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private QuestionsMapper questionsMapper;

    @Override
    @Transactional
    public QuestionsDTO createQuestion(QuestionsDTO questionsDTO, String id, String companyId) {
        Optional<JobPositionEntity> jobPositionEntityOptional = jobPositionRepository.findByIdAndCompanyId(questionsDTO.getJobPosition().getId(), companyId);
        if (!jobPositionEntityOptional.isPresent())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.JOB_POSITION_NOT_FOUND));
        questionsDTO.setCreateDate(new Date().getTime());
        questionsDTO.setStatus(QuestionStatus.ACTIVE);
        QuestionsEntity questionsEntity = questionsMapper.toEntity(questionsDTO);
        questionsEntity = questionsRepository.save(questionsEntity);
        return questionsMapper.toDTO(questionsEntity);
    }

    @Override
    @Transactional
    public Optional<QuestionsDTO> getQuestionById(String id, String companyId) {
        Optional<QuestionsEntity> questionsEntityOptional = questionsRepository.findByIdAndJobPositionCompanyId(id, companyId);
        return questionsEntityOptional.map(entity -> questionsMapper.toDTO(entity));
    }

    @Override
    public Page<QuestionsDTO> getQuestionsByCriteria(String companyId, String content, long fromDate, long toDate, QuestionStatus status, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        long date = new Date().getTime();
        if (fromDate == 0) fromDate = date - QuestionConstant.ONE_YEAR;
        if (toDate == 0) toDate = date + QuestionConstant.ONE_YEAR;
        if (offset < DataConstraint.Paging.OFFSET_MIN || pageSize < DataConstraint.Paging.PAGE_SIZE_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.INVALID_PAGE_NUMBER));
        if (fromDate > toDate)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Question.INVALID_DATE_RANGE));
        System.out.println("Content: " + content + " FromDate: " + fromDate + " ToDate: " + toDate + " Status: " + status);
        Page<QuestionsEntity> questionsEntityPage = questionsRepository.findAllByContentContainsAndCreateDateBetweenAndStatusAndJobPositionCompanyId(content, fromDate, toDate, status, companyId, PageRequest.of(offset, pageSize).withSort(Sort.by(direction, sortBy)));
        return questionsEntityPage.map(entity -> questionsMapper.toDTO(entity));
    }

    @Override
    public void deleteQuestion(String questionId, String companyId) {
        Optional<QuestionsEntity> questionsEntityOptional = questionsRepository.findByIdAndJobPositionCompanyId(questionId, companyId);
        if (!questionsEntityOptional.isPresent())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Question.NOT_FOUND));
        QuestionsEntity questionsEntity = questionsEntityOptional.get();
        questionsEntity.setStatus(QuestionStatus.INACTIVE);
        questionsRepository.save(questionsEntity);
    }

    @Override
    @Transactional
    public QuestionsDTO updateQuestion(QuestionsDTO dto, String userId, String companyId) {
        Optional<JobPositionEntity> jobPositionEntityOptional = jobPositionRepository.findByIdAndCompanyId(dto.getJobPosition().getId(), companyId);
        if (!jobPositionEntityOptional.isPresent())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.JOB_POSITION_NOT_FOUND));
        Optional<QuestionsEntity> questionsEntityOptional = questionsRepository.findByIdAndJobPositionCompanyId(dto.getId(), companyId);
        if (!questionsEntityOptional.isPresent())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Question.NOT_FOUND));
        dto.setUpdateDate(new Date().getTime());
        QuestionsEntity questionsEntity = questionsEntityOptional.get();
        questionsMapper.updateQuestion(dto, questionsEntity);
        questionsEntity = questionsRepository.save(questionsEntity);
        return questionsMapper.toDTO(questionsEntity);
    }

}
