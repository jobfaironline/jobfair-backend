package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.repositories.attendant.cv.QualificationRepository;
import org.capstone.job_fair.services.interfaces.attendant.cv.QualificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QualificationServiceImpl implements QualificationService {

    @Autowired
    private QualificationRepository qualificationRepository;

    @Override
    public Integer getCountQualificationById(String id) {
        return qualificationRepository.countById(id);
    }
}
