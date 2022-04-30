package org.capstone.job_fair.repositories.company.job.questions;

import org.capstone.job_fair.models.entities.company.job.questions.QuestionsEntity;
import org.capstone.job_fair.models.statuses.QuestionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionsRepository extends JpaRepository<QuestionsEntity, String> {

    @Query(value = "SELECT * FROM questions t WHERE t.jobPositionId = :jobPositionId AND t.status = 0 ORDER BY RAND() LIMIT :numberOfQuestion", nativeQuery = true)
    List<QuestionsEntity> getRandomQuestion(@Param("jobPositionId") String jobPositionId, @Param("numberOfQuestion") int numberOfQuestion);

    Optional<QuestionsEntity> findByIdAndJobPositionCompanyId(String id, String companyId);

    void deleteByIdAndJobPositionCompanyId(String id, String companyId);

    Page<QuestionsEntity> findAllByContentContainsAndJobPositionIdAndJobPositionCompanyIdAndStatus(String searchContent, String jobPositionId, String companyId, QuestionStatus status, Pageable pageable);

    Page<QuestionsEntity> findAllByContentContainsAndCreateDateBetweenAndStatusAndJobPositionCompanyId(String content, Long fromDate, Long toDate, QuestionStatus status, String companyId, Pageable pageable);

}
