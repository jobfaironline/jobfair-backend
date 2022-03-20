package org.capstone.job_fair.controllers.attendant.cv;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.requests.attendant.DraftCvRequest;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.CvDTO;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.mappers.attendant.cv.CvMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CvController {

    @Autowired
    private CvService cvService;

    @Autowired
    private CvMapper cvMapper;

    @PostMapping(ApiEndPoint.Cv.CV)
    public ResponseEntity<?> draftCV(@RequestBody @Valid DraftCvRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CvDTO dto = cvMapper.toDTO(request);
        AttendantDTO attendantDTO = new AttendantDTO();
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(userDetails.getId());
        attendantDTO.setAccount(accountDTO);
        dto.setAttendant(attendantDTO);

        dto = cvService.draftCv(dto);
        return ResponseEntity.ok(dto);
    }
}
