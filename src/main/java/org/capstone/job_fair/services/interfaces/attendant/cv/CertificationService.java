package org.capstone.job_fair.services.interfaces.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.CertificationDTO;

public interface CertificationService {
    void createNewCertification(CertificationDTO dto);
}
