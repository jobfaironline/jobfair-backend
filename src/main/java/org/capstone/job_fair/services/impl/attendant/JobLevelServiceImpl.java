package org.capstone.job_fair.services.impl.attendant;

import org.capstone.job_fair.repositories.attendant.JobLevelRepository;
import org.capstone.job_fair.services.interfaces.attendant.JobLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobLevelServiceImpl implements JobLevelService {

    @Autowired
    private JobLevelRepository jobLevelRepository;

    @Override
    public Integer getCountJobLevelById(Integer id) {
        return jobLevelRepository.countById(id);
    }

}
