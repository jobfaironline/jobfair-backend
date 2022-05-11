package org.capstone.job_fair.repositories.attendant.quiz;

import org.capstone.job_fair.models.entities.attendant.test.QuizQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizQuestionRepository  extends JpaRepository<QuizQuestionEntity, String> {


}
