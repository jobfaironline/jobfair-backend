package org.capstone.job_fair.repositories.attendant.cv.test;

import org.capstone.job_fair.models.entities.attendant.cv.test.QuizQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizQuestionRepository  extends JpaRepository<QuizQuestionEntity, String> {


}
