package org.capstone.job_fair.repositories.company.job.questions;

import org.capstone.job_fair.models.entities.company.job.questions.QuestionsEntity;
import org.capstone.job_fair.models.statuses.QuestionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionsRepository extends JpaRepository<QuestionsEntity, String> {

    Optional<QuestionsEntity> findByIdAndJobPositionCompanyId(String id, String companyId);

    void deleteByIdAndJobPositionCompanyId(String id, String companyId);

    Page<QuestionsEntity> findAllByJobPositionIdAndJobPositionCompanyId(String jobPositionId, String companyId, Pageable pageable);

    Page<QuestionsEntity> findAllByContentContainsAndCreateDateBetweenAndStatusAndJobPositionCompanyId(String content, Long fromDate, Long toDate, QuestionStatus status, String companyId, Pageable pageable);

}
