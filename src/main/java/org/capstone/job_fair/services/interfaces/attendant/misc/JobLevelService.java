package org.capstone.job_fair.services.interfaces.attendant.misc;

import org.capstone.job_fair.models.dtos.attendant.misc.JobLevelDTO;

import java.util.List;
import java.util.Optional;

public interface JobLevelService {
    Optional<JobLevelDTO> findById(int id);
    List<JobLevelDTO> getAll();
    JobLevelDTO delete(int id);
    JobLevelDTO create(JobLevelDTO dto);
    JobLevelDTO update(JobLevelDTO dto);
}
