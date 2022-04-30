package org.capstone.job_fair.repositories.attendant.cv.test;


import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
import org.capstone.job_fair.models.entities.attendant.cv.test.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, String> {
    Optional<QuizEntity> findByApplicationId(String applicationId);
}
