package org.capstone.job_fair.services.interfaces.company.job.question;

import org.capstone.job_fair.models.dtos.company.job.questions.QuestionsDTO;
import org.capstone.job_fair.models.dtos.util.ParseFileResult;
import org.capstone.job_fair.models.statuses.QuestionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface QuestionsService {
    QuestionsDTO createQuestion(QuestionsDTO questionsDTO, String id, String companyId);

    Optional<QuestionsDTO> getQuestionById(String id, String companyId);

    Page<QuestionsDTO> getQuestionsByCriteria(String companyId, String content, long fromDate, long toDate, QuestionStatus status, int pageSize, int offset, String sortBy, Sort.Direction direction);

    QuestionsDTO deleteQuestion(String questionId, String companyId);

    QuestionsDTO updateQuestion(QuestionsDTO dto, String companyId);

    Page<QuestionsDTO> getQuestionByJobPosition(String companyId, String jobPositionId, String searchContent, QuestionStatus status, int offset, int pageSize, String sortBy, Sort.Direction direction);

    ParseFileResult<QuestionsDTO> createNewQuestionsFromFile(MultipartFile file, String jobPositionId);
}
