package org.capstone.job_fair.controllers.attendant.cv;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.CVConstant;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.controllers.payload.requests.attendant.cv.UpdateCvRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.CvDTO;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.interfaces.matching_point.MatchingPointService;
import org.capstone.job_fair.services.interfaces.util.FileStorageService;
import org.capstone.job_fair.services.mappers.attendant.cv.CvMapper;
import org.capstone.job_fair.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class CvController {

    @Autowired
    private CvService cvService;

    @Autowired
    private CvMapper cvMapper;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private MatchingPointService matchingPointService;

    @PostMapping(ApiEndPoint.Cv.CV)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    public ResponseEntity<?> createCV() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CvDTO dto = new CvDTO();
        AttendantDTO attendantDTO = new AttendantDTO();
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(userDetails.getId());
        attendantDTO.setAccount(accountDTO);
        dto.setAttendant(attendantDTO);

        dto = cvService.draftCv(dto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping(ApiEndPoint.Cv.CV + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    public ResponseEntity<?> updateCV(@PathVariable("id") String id, @RequestBody @Valid UpdateCvRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userDetails.getId();
        CvDTO cvDTO = cvMapper.toDTO(request);
        cvDTO.setId(id);
        cvDTO = cvService.updateCV(cvDTO, userId);
        return ResponseEntity.ok(cvDTO);
    }

    @GetMapping(ApiEndPoint.Cv.CV)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    public ResponseEntity<?> getAll(
            @RequestParam(value = "offset", defaultValue = CVConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
            @RequestParam(value = "pageSize", defaultValue = CVConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = CVConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
            @RequestParam(value = "direction", required = false, defaultValue = CVConstant.DEFAULT_SEARCH_SORT_DIRECTION) Sort.Direction direction,
            @RequestParam(value = "name", defaultValue = CVConstant.DEFAULT_SEARCH_NAME) String name
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userDetails.getId();
        PageRequest pageRequest = PageRequest.of(offset, pageSize).withSort(Sort.by(direction, sortBy));
        Page<CvDTO> result = cvService.getAllByAttendantIdAndByName(userId, name, pageRequest);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @GetMapping(ApiEndPoint.Cv.CV + "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<CvDTO> result = cvService.getByIdAndAttendantId(id, userDetails.getId());
        if (!result.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.get());
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @DeleteMapping(ApiEndPoint.Cv.CV + "/{id}")
    public ResponseEntity<?> deleteCvById(@PathVariable String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CvDTO result = cvService.deleteCV(id, userDetails.getId());
        return ResponseEntity.ok(result);
    }


    @PostMapping(ApiEndPoint.Cv.UPLOAD_PROFILE_IMAGE)
    public ResponseEntity<?> uploadProfilePicture(@RequestParam("file") MultipartFile file, @RequestParam("cvId") String cvId) {
        CvDTO cvDTO;
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            byte[] image = ImageUtil.convertImage(file, DataConstraint.Account.IMAGE_TYPE, DataConstraint.Account.WIDTH_FACTOR, DataConstraint.Account.HEIGHT_FACTOR, DataConstraint.Account.IMAGE_EXTENSION_TYPE);
            String pictureProfileFolder = AWSConstant.CV_PROFILE_FOLDER;
            cvDTO = cvService.updateProfilePicture(pictureProfileFolder, cvId);
            fileStorageService.store(image, pictureProfileFolder + "/" + cvDTO.getId());
        } catch (IOException e) {
            return GenericResponse.build((e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.created(URI.create(cvDTO.getProfileImageUrl())).build();
    }

    @PostMapping(ApiEndPoint.Cv.GET_MATCHING_POINT)
    public ResponseEntity<?> getMatchingPoint(@RequestParam("cvId") String cvId, @RequestParam("boothJobPositionId") String boothJobPositionId) {
        Double point = matchingPointService.calculateBetweenCVAndBoothJobPosition(cvId, boothJobPositionId).block();
        Map<String, Double> result = new HashMap<>();
        result.put("result", point);
        return ResponseEntity.ok(result);
    }


}

