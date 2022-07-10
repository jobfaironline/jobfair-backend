package org.capstone.job_fair.services.interfaces.attendant.cv;

import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.CvDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CvService {
    CvDTO draftCv(CvDTO dto);

    Page<CvDTO> getAllByAttendantIdAndByName(String attendantId, String cvName, Pageable pageRequest);

    Optional<CvDTO> getByIdAndAttendantId(String id, String attendantId);

    CvDTO updateCV(CvDTO dto, String userId);

    CvDTO updateProfilePicture(String pictureProfileFolder, String id);

    CvDTO deleteCV(String cvId, String userId);
}
