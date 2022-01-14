package org.capstone.job_fair.services.interfaces.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.ReferenceDTO;

public interface ReferenceService {
    void createNewReference(ReferenceDTO dto);
}
