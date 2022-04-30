package org.capstone.job_fair.repositories.attendant.cv.test;

import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
import org.capstone.job_fair.models.entities.attendant.cv.test.QuizQuestionEntity;
import org.capstone.job_fair.models.entities.company.job.questions.QuestionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionRepository  extends JpaRepository<QuizQuestionEntity, String> {


}
