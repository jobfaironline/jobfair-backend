package org.capstone.job_fair.services.interfaces.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.CvDTO;

public interface CvService {
    CvDTO draftCv(CvDTO dto);
}
