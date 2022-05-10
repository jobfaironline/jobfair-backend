package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.InterviewRequestChangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRequestChangeRepository extends JpaRepository<InterviewRequestChangeEntity, String > {
    Optional<InterviewRequestChangeEntity> findTopByApplicationIdOrderByCreateTimeDesc(String applicationId);

    List<InterviewRequestChangeEntity> findByApplicationIdOrderByCreateTimeDesc(String applicationId);
}
