package org.capstone.job_fair.repositories.attendant.quiz;


import org.capstone.job_fair.models.entities.attendant.test.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, String> {
    Optional<QuizEntity> findByApplicationId(String applicationId);

    @Query("SELECT t FROM QuizEntity t WHERE t.beginTime < :currentTime AND t.endTime > :currentTime AND t.application.id = :applicationId AND t.application.attendant.accountId = :userId")
    Optional<QuizEntity> getQuiz(@Param("currentTime") Long currentTime, @Param("applicationId") String applicationId, @Param("userId") String userId);

    @Query("SELECT t FROM QuizEntity t WHERE t.application.attendant.accountId = :userId AND t.id = :quizId")
    Optional<QuizEntity> findQuizToSave(@Param("quizId") String quizId, @Param("userId") String userId);

    Optional<QuizEntity> findByIdAndApplicationAttendantAccountId(String id, String accountId);
}
