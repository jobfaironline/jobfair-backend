package org.capstone.job_fair.repositories.attendant.cv.test;


import org.capstone.job_fair.models.entities.attendant.cv.test.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, String> {
    Optional<QuizEntity> findByApplicationId(String applicationId);

    @Query("SELECT t FROM QuizEntity t WHERE t.beginTime < :currentTime AND t.endTime > :currentTime AND t.application.id = :applicationId")
    Optional<QuizEntity> getQuiz(Long currentTime, String applicationId);

    @Query("SELECT t FROM QuizEntity t WHERE t.id = :quizId AND t.application.id = :applicationId AND t.application.attendant.accountId = :userId")
    Optional<QuizEntity> findQuizByCriteria(String applicationId, String quizId, String userId);
}
