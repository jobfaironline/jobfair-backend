package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.repositories.attendant.JobLevelRepository;
import org.capstone.job_fair.services.interfaces.attendant.cv.JobLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobLevelServiceImpl implements JobLevelService {

    @Autowired
    private JobLevelRepository jobLevelRepository;

    @Override
    public Integer getCountJobLevelById(String id) {
        return jobLevelRepository.countById(id);
    }

    @Override
    public Optional<JobLevelEntity> getJobLevelById(int id) {
        return jobLevelRepository.findById(id);
    }
}
