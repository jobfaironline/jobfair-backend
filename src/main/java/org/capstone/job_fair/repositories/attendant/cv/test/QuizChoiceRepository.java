package org.capstone.job_fair.repositories.attendant.cv.test;

import org.capstone.job_fair.models.entities.attendant.cv.test.QuizChoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizChoiceRepository extends JpaRepository<QuizChoiceEntity, String> {
}
