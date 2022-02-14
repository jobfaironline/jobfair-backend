package org.capstone.job_fair.services.impl.attendant;

import org.capstone.job_fair.repositories.attendant.cv.QualificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QualificationServiceImpl {
    @Autowired
    private QualificationRepository qualificationRepository;

}
