package org.capstone.job_fair.services.interfaces.attendant.cv;

import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;

import java.util.Optional;

public interface JobLevelService {
    Integer getCountJobLevelById(String id);
    Optional<JobLevelEntity> getJobLevelById(int id);
}
