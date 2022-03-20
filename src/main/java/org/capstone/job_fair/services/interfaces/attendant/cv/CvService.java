package org.capstone.job_fair.services.interfaces.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.CvDTO;

import java.util.List;
import java.util.Optional;

public interface CvService {
    CvDTO draftCv(CvDTO dto);
    List<CvDTO> getAllByAttendantId(String attendantId);
    Optional<CvDTO> getById(String id);
}
