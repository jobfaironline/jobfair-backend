package org.capstone.job_fair.controllers.job_fair;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.*;
import org.capstone.job_fair.controllers.payload.requests.job_fair.DraftJobFairRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.UpdateJobFairRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.controllers.payload.responses.JobFairForAttendantResponse;
import org.capstone.job_fair.controllers.payload.responses.RenderJobFairParkResponse;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.JobFairBoothDTO;
import org.capstone.job_fair.models.dtos.company.JobFairBoothLayoutDTO;
import org.capstone.job_fair.models.dtos.dynamoDB.NotificationMessageDTO;
import org.capstone.job_fair.models.dtos.job_fair.AssignmentDTO;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.dtos.job_fair.LayoutDTO;
import org.capstone.job_fair.models.enums.NotificationType;
import org.capstone.job_fair.services.interfaces.company.CompanyBoothLayoutService;
import org.capstone.job_fair.services.interfaces.company.JobFairBoothService;
import org.capstone.job_fair.services.interfaces.dynamoDB.JobFairVisitService;
import org.capstone.job_fair.services.interfaces.dynamoDB.NotificationService;
import org.capstone.job_fair.services.interfaces.job_fair.AssignmentService;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairService;
import org.capstone.job_fair.services.interfaces.job_fair.LayoutService;
import org.capstone.job_fair.services.interfaces.util.FileStorageService;
import org.capstone.job_fair.services.mappers.job_fair.JobFairMapper;
import org.capstone.job_fair.utils.ImageUtil;
import org.capstone.job_fair.utils.MessageUtil;
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

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class JobFairController {

    @Autowired
    private JobFairBoothService jobFairBoothService;

    @Autowired
    private CompanyBoothLayoutService companyBoothLayoutService;

    @Autowired
    private LayoutService layoutService;

    @Autowired
    private JobFairService jobFairService;

    @Autowired
    private JobFairMapper jobFairMapper;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private JobFairVisitService jobFairVisitService;

    @GetMapping(ApiEndPoint.JobFair.FOR_3D_MAP + "/{id}")
    public ResponseEntity<?> getJobFairInformationFor3DMap(@PathVariable("id") String jobFairId) {
        RenderJobFairParkResponse response = new RenderJobFairParkResponse();

        Optional<LayoutDTO> layoutDTOOpt = layoutService.getByJobFairId(jobFairId);

        if (!layoutDTOOpt.isPresent()) {
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Layout.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        LayoutDTO layoutDTO = layoutDTOOpt.get();
        response.setJobFairLayoutUrl(layoutDTO.getUrl());

        layoutDTO.getBooths().forEach(boothDTO -> {
            Optional<JobFairBoothDTO> companyBoothOpt = jobFairBoothService.getCompanyBoothByJobFairIdAndBoothId(jobFairId, boothDTO.getId());
            if (companyBoothOpt.isPresent()) {
                JobFairBoothDTO companyBooth = companyBoothOpt.get();
                RenderJobFairParkResponse.BoothData boothData = new RenderJobFairParkResponse.BoothData();
                boothData.setPosition(boothDTO.getX(), boothDTO.getY(), boothDTO.getZ());
                boothData.setSlotName(boothDTO.getName());
                boothData.setCompanyBoothId(companyBooth.getId());
                boothData.setBoothName(companyBooth.getName());

                Optional<JobFairBoothLayoutDTO> layoutDTOOptional = companyBoothLayoutService.getLatestVersionByCompanyBoothId(companyBooth.getId());
                if (layoutDTOOptional.isPresent()) {
                    boothData.setBoothUrl(layoutDTOOptional.get().getUrl());
                    boothData.setCompanyBoothLayoutVideos(layoutDTOOptional.get().getCompanyBoothLayoutVideos());
                    response.addBoothDataInformation(boothData);
                }
            }
        });

        return ResponseEntity.ok(response);
    }

    @GetMapping(ApiEndPoint.JobFair.JOB_FAIR + "/{id}")
    public ResponseEntity<?> getJobFairDetailById(@PathVariable("id") String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<JobFairDTO> result = jobFairService.getById(id);
        if (!result.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        JobFairDTO jobFairDTO = result.get();
        return ResponseEntity.ok(jobFairDTO);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PostMapping(ApiEndPoint.JobFair.JOB_FAIR)
    public ResponseEntity<?> draftJobFair(@RequestBody DraftJobFairRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobFairDTO jobFairDTO = jobFairMapper.toDTO(request);
        jobFairDTO.setCompany(CompanyDTO.builder().id(userDetails.getCompanyId()).build());
        jobFairDTO = jobFairService.createNewJobFair(jobFairDTO);
        return ResponseEntity.ok(jobFairDTO);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PutMapping(ApiEndPoint.JobFair.JOB_FAIR)
    public ResponseEntity<?> updateJobFair(@RequestBody UpdateJobFairRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobFairDTO jobFairDTO = jobFairMapper.toDTO(request);
        jobFairDTO = jobFairService.updateJobFair(jobFairDTO, userDetails.getCompanyId());
        return ResponseEntity.ok(jobFairDTO);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @DeleteMapping(ApiEndPoint.JobFair.JOB_FAIR + "/{id}")
    public ResponseEntity<?> deleteJobFair(@PathVariable("id") String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobFairDTO jobFairDTO = jobFairService.deleteJobFair(id, userDetails.getCompanyId());
        return ResponseEntity.ok(jobFairDTO);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.JobFair.JOB_FAIR)
    public ResponseEntity<?> getJobFair(@RequestParam(value = "offset", defaultValue = JobFairConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset, @RequestParam(value = "pageSize", defaultValue = JobFairConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize, @RequestParam(value = "sortBy", defaultValue = JobFairConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy, @RequestParam(value = "direction", required = false, defaultValue = JobFairConstant.DEFAULT_SEARCH_SORT_DIRECTION) Sort.Direction direction, @RequestParam(value = "name", defaultValue = JobFairConstant.DEFAULT_JOBFAIR_NAME) String name) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<JobFairDTO> result = jobFairService.findByNameAndCompanyId(name, userDetails.getCompanyId(), PageRequest.of(offset, pageSize).withSort(Sort.by(direction, sortBy)));
        return ResponseEntity.ok(result);

    }


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PostMapping(ApiEndPoint.JobFair.PUBLISH + "/{jobFairId}")
    public ResponseEntity<?> publishJobFair(@PathVariable("jobFairId") String jobFairPlanId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jobFairService.publishJobFair(userDetails.getCompanyId(), jobFairPlanId);

        List<AssignmentDTO> assignments = assignmentService.getAssignmentByJobFairId(jobFairPlanId, userDetails.getCompanyId());
        assignments.forEach(assignment -> {
            NotificationMessageDTO notificationMessage = NotificationMessageDTO.builder()
                    .title(MessageUtil.getMessage(MessageConstant.NotificationMessage.ASSIGN_EMPLOYEE.TITLE))
                    .message(MessageUtil.getMessage(MessageConstant.NotificationMessage.ASSIGN_EMPLOYEE.MESSAGE))
                    .notificationType(NotificationType.NOTI)
                    .userId(assignment.getCompanyEmployee().getAccountId()).build();
            notificationService.createNotification(notificationMessage, assignment.getCompanyEmployee().getAccountId());
        });



        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiEndPoint.JobFair.UPLOAD_THUMBNAIL + "/{jobFairId}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) OR hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @SneakyThrows
    public ResponseEntity<?> uploadThumbnail(@PathVariable("jobFairId") String jobFairId, @RequestParam("file") MultipartFile file) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();

        byte[] image = ImageUtil.convertImage(file, DataConstraint.JobFair.IMAGE_TYPE, DataConstraint.JobFair.WIDTH_FACTOR, DataConstraint.JobFair.HEIGHT_FACTOR, DataConstraint.JobFair.IMAGE_EXTENSION_TYPE);
        JobFairDTO jobFairDTO = jobFairService.createOrUpdateJobFairThumbnail(AWSConstant.JOBFAIR_THUMBNAIL_FOLDER, jobFairId, companyId);

        fileStorageService.store(image, AWSConstant.JOBFAIR_THUMBNAIL_FOLDER + "/" + jobFairDTO.getId());
        return ResponseEntity.ok(jobFairDTO);
    }

    @GetMapping(ApiEndPoint.JobFair.FOR_ATTENDANT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    public ResponseEntity<?> getJobFairForAttendant(
            @RequestParam(value = "offset", defaultValue = JobFairConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
            @RequestParam(value = "pageSize", defaultValue = JobFairConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = JobFairConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
            @RequestParam(value = "direction", required = false, defaultValue = JobFairConstant.DEFAULT_SEARCH_SORT_DIRECTION) Sort.Direction direction,
            @RequestParam(value = "name", defaultValue = JobFairConstant.DEFAULT_JOBFAIR_NAME) String name,
            @RequestParam(value = "categoryId", defaultValue = JobFairConstant.DEFAULT_CATEGORY_ID) String categoryId,
            @RequestParam(value = "countryId", defaultValue = JobFairConstant.DEFAULT_COUNTRY_ID) String countryId) {
        Page<JobFairDTO> result = jobFairService.findJobFairForAttendantByCriteria(name, countryId, categoryId, PageRequest.of(offset, pageSize).withSort(Sort.by(direction, sortBy)));
        Page<JobFairForAttendantResponse> responses = result.map(JobFairForAttendantResponse::new).map(response -> {
            int count = jobFairVisitService.getCurrentVisitOfJobFair(response.getId());
            response.setVisitCount(count);
            return response;
        });
        return ResponseEntity.ok(responses);
    }

}
