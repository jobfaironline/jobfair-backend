package org.capstone.job_fair.services.interfaces.company.question;

import org.capstone.job_fair.controllers.payload.responses.QuestionResponse;
import org.capstone.job_fair.models.dtos.company.job.questions.QuestionsDTO;
import org.capstone.job_fair.models.statuses.QuestionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public interface QuestionsService {
    QuestionResponse createQuestion(QuestionsDTO questionsDTO, String id, String companyId);

    Optional<QuestionResponse> getQuestionById(String id, String companyId);

    Page<QuestionResponse> getQuestionsByCriteria(String companyId, String content, long fromDate, long toDate, QuestionStatus status, int pageSize, int offset, String sortBy, Sort.Direction direction);

    QuestionResponse deleteQuestion(String questionId, String companyId);

    public QuestionResponse updateQuestion(QuestionsDTO dto, String companyId);

    Page<QuestionResponse> getQuestionByJobPosition(String companyId, String jobPositionId, int offset, int pageSize, String sortBy, Sort.Direction direction);
}
