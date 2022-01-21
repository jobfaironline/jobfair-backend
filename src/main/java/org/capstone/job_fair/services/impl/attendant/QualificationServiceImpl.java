package org.capstone.job_fair.services.impl.attendant;

import org.capstone.job_fair.repositories.attendant.cv.QualificationRepository;
import org.capstone.job_fair.services.interfaces.attendant.QualificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QualificationServiceImpl implements QualificationService {
    @Autowired
    private QualificationRepository qualificationRepository;


    @Override
    public Integer getCountById(int id) {
        return qualificationRepository.countById(id);
    }
}
