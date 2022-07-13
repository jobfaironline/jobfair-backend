package org.capstone.job_fair.controllers.demo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.account.cv.CreateApplicationRequest;
import org.capstone.job_fair.controllers.payload.requests.attendant.EvaluateApplicationRequest;
import org.capstone.job_fair.controllers.payload.requests.attendant.RegisterAttendantRequest;
import org.capstone.job_fair.controllers.payload.requests.demo.CreateApplicationAndEvaluateRequest;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.application.ApplicationDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.CvDTO;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.dtos.dynamoDB.NotificationMessageDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.BoothJobPositionDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothDTO;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.models.enums.NotificationType;
import org.capstone.job_fair.repositories.attendant.cv.CvRepository;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.services.interfaces.attendant.AttendantService;
import org.capstone.job_fair.services.interfaces.attendant.application.ApplicationService;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.interfaces.company.job.JobPositionService;
import org.capstone.job_fair.services.interfaces.job_fair.booth.JobFairBoothService;
import org.capstone.job_fair.services.interfaces.matching_point.MatchingPointService;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Slf4j
public class DemoController {


    @Autowired
    private AttendantMapper attendantMapper;

    @Autowired
    private AttendantService attendantService;


    @Autowired
    private CvService cvService;

    @Autowired
    private JobFairBoothService jobFairBoothService;

    @Autowired
    private JobPositionService jobPositionService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private MatchingPointService matchingPointService;

    @Autowired
    private JobFairRepository jobFairRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CvRepository cvRepository;

    private String getSaltString(int number) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < number) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    private String generatePhoneNumber() {
        String phoneNumber = "";
        for (int i = 0; i < 10; i++) {
            phoneNumber += new Random().nextInt(9);
        }
        return phoneNumber;
    }


    private String createAttendant(int i) {
        RegisterAttendantRequest request = new RegisterAttendantRequest();
        request.setEmail(getSaltString(10) + "@gmail.com");
        request.setPassword("123456");
        request.setFirstname("Nguyen Van");
        request.setLastname("So " + i);
        request.setPhone(generatePhoneNumber());
        request.setMiddlename("MiddleName " + i);

        AttendantDTO attendantDTO = attendantMapper.toDTO(request);

        attendantDTO = attendantService.createNewAccount(attendantDTO);

        return attendantDTO.getAccount().getEmail() + "," + attendantDTO.getAccount().getId();
    }

    private String createCV(String attendantId) {
        CvDTO dto = new CvDTO();
        AttendantDTO attendantDTO = new AttendantDTO();
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(attendantId);
        attendantDTO.setAccount(accountDTO);
        dto.setAttendant(attendantDTO);

        dto = cvService.draftCv(dto);
        return dto.getId();
    }

    private List<BoothJobPositionDTO> createBoothJobPosition(String jobFairId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Page<JobPositionDTO> jobPositions = jobPositionService.getAllJobPositionOfCompany(userDetails.getCompanyId(), null, null, null, 5, 0, "createdDate", Sort.Direction.ASC);

        //Get all job fair booths of job fair, then add job position to those booths
        List<JobFairBoothDTO> boothListDTO = jobFairBoothService.getCompanyBoothByJobFairId(jobFairId);
        int count = 0;
        for (JobFairBoothDTO dto : boothListDTO) {
            count++;
            JobFairBoothDTO jobFairBoothDto = new JobFairBoothDTO();
            jobFairBoothDto.setId(dto.getId());
            jobFairBoothDto.setDescription("chay script lan thu " + count);
            jobFairBoothDto.setName("chay script lan thu " + count);
            //mapping job position to booth job position
            List<BoothJobPositionDTO> boothJobPositions = jobPositions.getContent().stream().map(item -> {
                BoothJobPositionDTO boothJobPosition = BoothJobPositionDTO.builder().originJobPosition(item.getId()).minSalary(Double.parseDouble("5")).maxSalary(Double.parseDouble("5")).numOfPosition(Integer.parseInt("1")).isHaveTest(false).note("abc").testTimeLength(Integer.parseInt("15")).numOfQuestion(Integer.parseInt("0")).passMark(Double.parseDouble("0")).jobFairBooth(jobFairBoothDto).descriptionKeyWord(item.getDescriptionKeyWord()).requirementKeyWord(item.getRequirementKeyWord()).build();
                boothJobPosition.setIsHaveTest(false);
                return boothJobPosition;
            }).collect(Collectors.toList());
            dto.setBoothJobPositions(boothJobPositions);
            jobFairBoothService.updateJobFairBooth(dto, userDetails.getCompanyId());
        }


        List<BoothJobPositionDTO> result = new ArrayList<>();
        jobFairBoothService.getCompanyBoothByJobFairId(jobFairId).stream().forEach(item -> item.getBoothJobPositions().stream().forEach(element -> result.add(element)));
        return result;
    }


    private List<Map<String, String>> createAttendantsWithOneCv(Integer numberOfAttendants) {

        List<Map<String, String>> result = new ArrayList<>();


        String attendantEmail = null;
        String attendantId = null;

        for (int i = 0; i < numberOfAttendants; i++) {
            Map<String, String> map = new HashMap<String, String>();
            //Create attendant
            attendantEmail = createAttendant(i).split(",")[0];
            attendantId = createAttendant(i).split(",")[1];

            //Create CV
            String cvId = createCV(attendantId);
            map.put(attendantId, cvId);
            result.add(map);
        }
        return result;

    }

    @GetMapping(ApiEndPoint.Demo.CREATE_ATTENDANT)
    public ResponseEntity<?> createAttendantWithCV(@RequestParam Integer numberOfAttendants) {
        return ResponseEntity.ok(createAttendantsWithOneCv(numberOfAttendants));
    }

    @GetMapping(ApiEndPoint.Demo.CREATE_BOOTH_JOB_POSITION)
    public ResponseEntity<?> createBoothJobPositions(@RequestParam String jobFairId) {
        return ResponseEntity.ok(createBoothJobPosition(jobFairId));
    }

    private String applyApplication(CreateApplicationRequest request, String accountId) {
        //call applicationDTO
        ApplicationDTO dto = new ApplicationDTO();

        AttendantDTO attendantDTO = new AttendantDTO();
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(accountId);
        attendantDTO.setAccount(accountDTO);
        dto.setAttendant(attendantDTO);

        //call registrationJobPositionDTO + setId from request
        BoothJobPositionDTO regisDTO = new BoothJobPositionDTO();
        regisDTO.setId(request.getBoothJobPositionId());
        //set summary, create date, status, attendantDTO, registrationJobPositionDTO for ApplicationDTO
        dto.setCreateDate(new Date().getTime());
        dto.setStatus(ApplicationStatus.DRAFT);
        dto.setOriginCvId(request.getCvId());
        dto.setBoothJobPositionDTO(regisDTO);
        //call create method
        ApplicationDTO result = applicationService.createNewApplication(dto);
        matchingPointService.calculateFromApplication(result.getId()).subscribe().dispose();
        applicationService.submitApplication(result.getId(), accountId);
        return result.getId();
    }

    private void evaluateApplication(EvaluateApplicationRequest request, String userId) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(request.getApplicationId());
        dto.setEvaluateMessage(request.getEvaluateMessage());
        dto.setStatus(request.getStatus());

        dto = applicationService.evaluateApplication(dto, userId);

        //create notification message
        NotificationMessageDTO notificationMessageDTO = new NotificationMessageDTO();
        notificationMessageDTO.setMessage(MessageUtil.getMessage(MessageConstant.Application.EVALUATE_MESSAGE_TO_ATTENDANT));
        notificationMessageDTO.setNotificationType(NotificationType.NOTI);

        //send notification to attendant
        notificationService.createNotification(notificationMessageDTO, dto.getAttendant().getAccount().getId());
    }

    @PostMapping(ApiEndPoint.Demo.SUBMIT_MULTIPLE_APPLICATION)
    public ResponseEntity<?> createApplicationAndEvaluate(@RequestBody CreateApplicationAndEvaluateRequest request) {
        List<String> cvIdList = request.getCvId();
        final double numberOfApprove = cvIdList.size() * 0.3;
        final double numberOfPending = cvIdList.size() * 0.3;
        final double numberOfReject = cvIdList.size() - numberOfApprove - numberOfPending;
        Map<String, ApplicationStatus> result = new HashMap<>();
        int i = 0;
        //Approve
        for (; i < numberOfApprove; i++) {
            String cv = cvIdList.get(i);
            String accountId = cvRepository.findById(cv).get().getAttendant().getAccountId();
            CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
            createApplicationRequest.setBoothJobPositionId(request.getBoothJobPosition());
            createApplicationRequest.setCvId(cv);
            String applicationId = applyApplication(createApplicationRequest, accountId);
            EvaluateApplicationRequest evaluateApplicationRequest = new EvaluateApplicationRequest();
            evaluateApplicationRequest.setApplicationId(applicationId);
            evaluateApplicationRequest.setStatus(ApplicationStatus.APPROVE);
            evaluateApplicationRequest.setEvaluateMessage("Script auto evaluation approve");
            result.put(applicationId, ApplicationStatus.APPROVE);
        }
        //Pending
        for (; i < numberOfApprove + numberOfReject + numberOfPending; i++) {
            String cv = cvIdList.get(i);
            String accountId = cvRepository.findById(cv).get().getAttendant().getAccountId();
            CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
            createApplicationRequest.setBoothJobPositionId(request.getBoothJobPosition());
            createApplicationRequest.setCvId(cv);
            String applicationId = applyApplication(createApplicationRequest, accountId);
            EvaluateApplicationRequest evaluateApplicationRequest = new EvaluateApplicationRequest();
            evaluateApplicationRequest.setApplicationId(applicationId);
            evaluateApplicationRequest.setStatus(ApplicationStatus.PENDING);
            evaluateApplicationRequest.setEvaluateMessage("Script auto evaluation pending " + i);
            result.put(applicationId, ApplicationStatus.PENDING);
        }
        //Reject
        for (; i < numberOfApprove + numberOfReject; i++) {
            String cv = cvIdList.get(i);
            String accountId = cvRepository.findById(cv).get().getAttendant().getAccountId();
            CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
            createApplicationRequest.setBoothJobPositionId(request.getBoothJobPosition());
            createApplicationRequest.setCvId(cv);
            String applicationId = applyApplication(createApplicationRequest, accountId);
            EvaluateApplicationRequest evaluateApplicationRequest = new EvaluateApplicationRequest();
            evaluateApplicationRequest.setApplicationId(applicationId);
            evaluateApplicationRequest.setStatus(ApplicationStatus.REJECT);
            evaluateApplicationRequest.setEvaluateMessage("Script auto evaluation reject " + i);
            result.put(applicationId, ApplicationStatus.REJECT);
        }

        return ResponseEntity.ok(result);
    }


}
