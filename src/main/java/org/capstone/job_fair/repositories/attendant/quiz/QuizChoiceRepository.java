package org.capstone.job_fair.repositories.attendant.quiz;

import org.capstone.job_fair.models.entities.attendant.test.QuizChoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizChoiceRepository extends JpaRepository<QuizChoiceEntity, String> {
}
