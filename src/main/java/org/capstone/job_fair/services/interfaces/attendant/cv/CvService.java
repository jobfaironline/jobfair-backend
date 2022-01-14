package org.capstone.job_fair.services.interfaces.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.CvDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;

public interface CvService {
    void createNewCv(CvDTO dto);
    Integer getCountCvByEmail(String email);
    Integer getCountCvByID(String id);
}
