package org.capstone.job_fair.controllers.demo;

import com.amazonaws.util.json.Jackson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.account.cv.CreateApplicationRequest;
import org.capstone.job_fair.controllers.payload.requests.attendant.EvaluateApplicationRequest;
import org.capstone.job_fair.controllers.payload.requests.attendant.RegisterAttendantRequest;
import org.capstone.job_fair.controllers.payload.requests.company.CompanyEmployeeRegisterRequest;
import org.capstone.job_fair.controllers.payload.requests.company.CreateCompanyRequest;
import org.capstone.job_fair.controllers.payload.requests.demo.CreateApplicationAndEvaluateRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.DraftJobFairRequest;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.application.ApplicationDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.CvDTO;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.dtos.dynamoDB.NotificationMessageDTO;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.dtos.job_fair.JobFairProgressDTO;
import org.capstone.job_fair.models.dtos.job_fair.ShiftDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.BoothJobPositionDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.entities.job_fair.ShiftEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.AssignmentEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.JobFairBoothLayoutEntity;
import org.capstone.job_fair.models.enums.*;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.capstone.job_fair.repositories.attendant.application.ApplicationRepository;
import org.capstone.job_fair.repositories.attendant.cv.CvRepository;
import org.capstone.job_fair.repositories.company.CompanyEmployeeRepository;
import org.capstone.job_fair.repositories.company.job.JobPositionRepository;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.AssignmentRepository;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.JobFairBoothLayoutRepository;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.JobFairBoothRepository;
import org.capstone.job_fair.services.interfaces.attendant.AttendantService;
import org.capstone.job_fair.services.interfaces.attendant.application.ApplicationService;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.interfaces.company.job.JobPositionService;
import org.capstone.job_fair.services.interfaces.company.job.question.QuestionsService;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairService;
import org.capstone.job_fair.services.interfaces.job_fair.LayoutService;
import org.capstone.job_fair.services.interfaces.job_fair.booth.AssignmentService;
import org.capstone.job_fair.services.interfaces.job_fair.booth.JobFairBoothService;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.capstone.job_fair.services.mappers.company.CompanyEmployeeMapper;
import org.capstone.job_fair.services.mappers.company.CompanyMapper;
import org.capstone.job_fair.services.mappers.company.job.JobPositionMapper;
import org.capstone.job_fair.services.mappers.job_fair.JobFairMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.capstone.job_fair.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Transactional
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
    private JobFairBoothRepository jobFairBoothRepository;

    @Autowired
    private JobPositionService jobPositionService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private JobFairRepository jobFairRepository;

    @Autowired
    @Qualifier("LocalNotificationService")
    private NotificationService notificationService;

    @Autowired
    private CvRepository cvRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private CompanyEmployeeService companyEmployeeService;

    @Autowired
    private CompanyEmployeeMapper companyEmployeeMapper;

    @Autowired
    private QuestionsService questionsService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JobFairMapper jobFairMapper;

    @Autowired
    private JobFairService jobFairService;

    @Autowired
    private CompanyEmployeeRepository companyEmployeeRepository;

    @Autowired
    private LayoutService layoutService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobFairBoothLayoutRepository jobFairBoothLayoutRepository;


    @Autowired
    private JobPositionMapper jobPositionMapper;

    @Autowired
    private JobPositionRepository jobPositionRepository;

    @Autowired
    private Clock clock;

    private Random random = new Random();

    private String getSaltString(int number) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        while (salt.length() < number) { // length of the random string.
            int index = (int) (random.nextFloat() * SALTCHARS.length());
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


    private AttendantDTO createAttendant(int i) {
        RegisterAttendantRequest request = new RegisterAttendantRequest();
        request.setEmail(getSaltString(10) + "@gmail.com");
        request.setPassword("123456");
        request.setFirstname("Adam");
        request.setLastname("Smith");
        request.setPhone(generatePhoneNumber());
        request.setMiddlename(getSaltString(3));

        AttendantDTO attendantDTO = attendantMapper.toDTO(request);

        attendantDTO = attendantService.createNewAccount(attendantDTO);

        return attendantDTO;
    }

    private String createCV(AttendantDTO attendantDTO) {
        CvDTO dto = new CvDTO();
        dto.setAttendant(attendantDTO);
        dto.setEmail(attendantDTO.getAccount().getEmail());
        dto.setCreateTime(clock.millis());
        dto.setName("My cv");
        dto.setAboutMe("This is me");
        dto.setCountryId(attendantDTO.getCountryId());
        dto.setFullName(attendantDTO.getAccount().getFirstname() + " " + attendantDTO.getAccount().getMiddlename() + " " + attendantDTO.getAccount().getLastname());
        dto.setJobLevel(attendantDTO.getJobLevel());
        dto.setJobTitle(attendantDTO.getJobTitle());
        dto.setPhone(attendantDTO.getAccount().getPhone());
        dto.setProfileImageUrl(attendantDTO.getAccount().getProfileImageUrl());
        dto = cvService.draftCv(dto);
        return dto.getId();
    }

    //This function must be call when Supervisor edit booth profile. No add booth job position manually and must run script
    //REMEMBER: run script to delete all application first !
    private List<BoothJobPositionDTO> createBoothJobPosition(String jobFairId) {

        Optional<JobFairEntity> jobFairOpt = jobFairRepository.findById(jobFairId);
        if (!jobFairOpt.isPresent()) {
            return null;
        }

        JobFairEntity jobFair = jobFairOpt.get();
        CompanyEntity companyEntity = jobFair.getCompany();

        //get 5 job positions first
        Page<JobPositionDTO> jobPositions = jobPositionService.getAllJobPositionOfCompany(companyEntity.getId(), null, null, null, 5, 0, "createdDate", Sort.Direction.ASC);

        //Get all job fair booths of job fair, then add job position to those booths
        List<JobFairBoothDTO> boothListDTO = jobFairBoothService.getCompanyBoothByJobFairId(jobFairId);

        int count = 0;
        List<BoothJobPositionDTO> result = new ArrayList<>();

        for (JobFairBoothDTO dto : boothListDTO) {
            count++;
            JobFairBoothDTO jobFairBoothDto = new JobFairBoothDTO();
            jobFairBoothDto.setId(dto.getId());
            jobFairBoothDto.setDescription("chay script lan thu " + count);
            jobFairBoothDto.setName("chay script lan thu " + count);
            //mapping job position to booth job position
            List<BoothJobPositionDTO> boothJobPositions = jobPositions.getContent().stream().map(item -> BoothJobPositionDTO
                    .builder()
                    .originJobPosition(item.getId())
                    .minSalary(5.0)
                    .maxSalary(100.0)
                    .numOfPosition(1)
                    .isHaveTest(true)
                    .note("this job position required test")
                    .testTimeLength(15)
                    .numOfQuestion(4)
                    .passMark(1.0)
                    .jobFairBooth(jobFairBoothDto)
                    .descriptionKeyWord(item.getDescriptionKeyWord())
                    .requirementKeyWord(item.getRequirementKeyWord()).build()).collect(Collectors.toList());
            dto.setBoothJobPositions(boothJobPositions);
            dto = jobFairBoothService.updateJobFairBooth(dto, companyEntity.getId());
            result.addAll(dto.getBoothJobPositions());
        }
        return result;
    }

    private List<BoothJobPositionDTO> createBoothJobPosition(String jobFairId, String jobFairBoothId) {

        Optional<JobFairEntity> jobFairOpt = jobFairRepository.findById(jobFairId);
        if (!jobFairOpt.isPresent()) {
            return null;
        }

        JobFairEntity jobFair = jobFairOpt.get();
        CompanyEntity companyEntity = jobFair.getCompany();

        //get 5 job positions first
        Page<JobPositionDTO> jobPositions = jobPositionService.getAllJobPositionOfCompany(companyEntity.getId(), null, null, null, 5, 0, "createdDate", Sort.Direction.ASC);

        //Get all job fair booths of job fair, then add job position to those booths
        List<JobFairBoothDTO> boothListDTO = jobFairBoothService.getCompanyBoothByJobFairId(jobFairId);

        int count = 0;
        List<BoothJobPositionDTO> result = new ArrayList<>();

        for (JobFairBoothDTO dto : boothListDTO) {
            if (dto.getId().equals(jobFairBoothId)) continue;
            count++;
            JobFairBoothDTO jobFairBoothDto = new JobFairBoothDTO();
            jobFairBoothDto.setId(dto.getId());
            jobFairBoothDto.setDescription("chay script lan thu " + count);
            jobFairBoothDto.setName("chay script lan thu " + count);
            //mapping job position to booth job position
            List<BoothJobPositionDTO> boothJobPositions = jobPositions.getContent().stream().map(item -> BoothJobPositionDTO
                    .builder()
                    .originJobPosition(item.getId())
                    .minSalary(5.0)
                    .maxSalary(100.0)
                    .numOfPosition(1)
                    .isHaveTest(true)
                    .note("this job position required test")
                    .testTimeLength(15)
                    .numOfQuestion(4)
                    .passMark(1.0)
                    .jobFairBooth(jobFairBoothDto)
                    .descriptionKeyWord(item.getDescriptionKeyWord())
                    .requirementKeyWord(item.getRequirementKeyWord()).build()).collect(Collectors.toList());
            dto.setBoothJobPositions(boothJobPositions);
            dto = jobFairBoothService.updateJobFairBooth(dto, companyEntity.getId());
            result.addAll(dto.getBoothJobPositions());
        }
        return result;
    }

    private List<String> createAttendantsWithOneCv(Integer numberOfAttendants) {
        List<String> result = new ArrayList<>();

        for (int i = 0; i < numberOfAttendants; i++) {
            Map<String, String> map = new HashMap<String, String>();
            //Create attendant
            AttendantDTO attendantDTO = createAttendant(i);

            //Create CV
            String cvId = createCV(attendantDTO);
            result.add(cvId);
        }
        return result;

    }


    @GetMapping(ApiEndPoint.Demo.CREATE_ATTENDANT)
    public ResponseEntity<?> createAttendantWithCV(@RequestParam Integer numberOfAttendants) {
        return ResponseEntity.ok(createAttendantsWithOneCv(numberOfAttendants));
    }

    @GetMapping(ApiEndPoint.Demo.PUBLISH_JOBFAIR)
    public ResponseEntity<?> publishJobFair(@RequestParam String jobFairId) {
        JobFairEntity jobFair = jobFairRepository.findById(jobFairId).get();
        jobFair.setPublicStartTime(jobFair.getCreateTime());
        jobFairRepository.save(jobFair);
        return ResponseEntity.ok().build();
    }

    protected void decorateJobFair(String jobFairId, String jobFairBoothId){

        List<String> defaultBoothUrls = Arrays.asList("https://d1t63ajhfi2lx8.cloudfront.net/Default/default_booth.glb", "https://d1t63ajhfi2lx8.cloudfront.net/Default/default2.glb", "https://d1t63ajhfi2lx8.cloudfront.net/Default/default3.glb", "https://d1t63ajhfi2lx8.cloudfront.net/Default/default4.glb");
        JobFairEntity jobFair = jobFairRepository.getById(jobFairId);
        long oldDecorateEndTime = jobFair.getDecorateEndTime();
        long oldDecorateStartTime = jobFair.getDecorateStartTime();
        jobFair.setDecorateStartTime(0L);
        jobFair.setDecorateEndTime(clock.millis()+ 10000000);
        jobFairRepository.saveAndFlush(jobFair);

        //get booths that have employee assign to
        createBoothJobPosition(jobFairId, jobFairBoothId);



        //make latest booth layout, assign to booth
        JobFairProgressDTO jobFairProgressDTO = jobFairService.getJobFairProgress(jobFairId);

        for (JobFairProgressDTO.Booth booth: jobFairProgressDTO.getBooths()){
            if (booth.getId().equals(jobFairBoothId)) continue;
            JobFairBoothEntity jobFairBooth = jobFairBoothRepository.getById(booth.getId());
            jobFairBooth.setDescription("This is department description");
            JobFairBoothLayoutEntity layout = new JobFairBoothLayoutEntity();
            layout.setId(UUID.randomUUID().toString());
            layout.setVersion(1);
            layout.setCreateDate(clock.millis());
            String url = defaultBoothUrls.get(random.nextInt(defaultBoothUrls.size()));
            System.out.println(url);
            layout.setUrl(url);
            layout.setJobFairBooth(jobFairBooth);
            jobFairBoothLayoutRepository.save(layout);
            jobFairBoothRepository.save(jobFairBooth);
        }

        jobFair.setDecorateEndTime(oldDecorateEndTime);
        jobFair.setDecorateStartTime(oldDecorateStartTime);
        jobFairRepository.saveAndFlush(jobFair);
    }

    private void assignTask(String jobFairId, String jobFairBoothId){
        JobFairEntity jobFair = jobFairRepository.getById(jobFairId);
        List<ShiftEntity> shifts = jobFair.getShifts();
        ShiftEntity morningShift, afternoonShift;
        //Get morning shift and afternoon shift
        if (shifts.get(0).getBeginTime() < shifts.get(1).getBeginTime()) {
            morningShift = shifts.get(0);
            afternoonShift = shifts.get(1);
        } else {
            morningShift = shifts.get(1);
            afternoonShift = shifts.get(0);
        }

        long publicStartTime = jobFair.getPublicStartTime();
        long publicEndTime = jobFair.getPublicEndTime();
        LocalDateTime publicStartDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(publicStartTime), ZoneId.systemDefault());
        LocalDateTime publicEndDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(publicEndTime), ZoneId.systemDefault());
        long daysBetween = Duration.between(publicStartDateTime, publicEndDateTime).toDays();


        List<JobFairBoothEntity> booths = jobFair.getJobFairBoothList();
        for (JobFairBoothEntity booth : booths){
            if (Objects.equals(booth.getId(), jobFairBoothId)) continue;
            if (booth.getName() == null) continue;
            List<AssignmentEntity> staffs = assignmentRepository.findByJobFairBoothIdAndType(booth.getId(), AssignmentType.STAFF);
            List<AssignmentEntity> supervisors = assignmentRepository.findByJobFairBoothIdAndType(booth.getId(), AssignmentType.SUPERVISOR);
            AssignmentEntity staff1 = staffs.get(0);
            AssignmentEntity staff2 = staffs.get(1);
            AssignmentEntity supervisor = supervisors.get(0);

            for (int i = 0; i <= daysBetween; i ++){
                long beginDate = publicStartDateTime.plusDays(i).toLocalDate().atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
                assignmentService.assignEmployee(supervisor.getCompanyEmployee().getAccountId(),
                        staff1.getCompanyEmployee().getAccountId(),
                        booth.getId(), AssignmentType.INTERVIEWER,
                        jobFair.getCompany().getId(), beginDate + morningShift.getBeginTime(), beginDate + morningShift.getEndTime());
                assignmentService.assignEmployee(supervisor.getCompanyEmployee().getAccountId(),
                        staff1.getCompanyEmployee().getAccountId(),
                        booth.getId(), AssignmentType.INTERVIEWER,
                        jobFair.getCompany().getId(), beginDate + afternoonShift.getBeginTime(), beginDate + afternoonShift.getEndTime());
            }

            for (int i = 0; i <= daysBetween; i ++){
                long beginDate = publicStartDateTime.plusDays(i).toLocalDate().atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
                assignmentService.assignEmployee(supervisor.getCompanyEmployee().getAccountId(),
                        staff2.getCompanyEmployee().getAccountId(),
                        booth.getId(), AssignmentType.RECEPTION,
                        jobFair.getCompany().getId(), beginDate + morningShift.getBeginTime(), beginDate + morningShift.getEndTime());
                assignmentService.assignEmployee(supervisor.getCompanyEmployee().getAccountId(),
                        staff2.getCompanyEmployee().getAccountId(),
                        booth.getId(), AssignmentType.RECEPTION,
                        jobFair.getCompany().getId(), beginDate + afternoonShift.getBeginTime(), beginDate + afternoonShift.getEndTime());
            }
        }
    }

    @GetMapping(ApiEndPoint.Demo.CREATE_BOOTH_JOB_POSITION)
    public ResponseEntity<?> createBoothJobPositions(@RequestParam String jobFairId, @RequestParam String jobFairBoothId) {
        decorateJobFair(jobFairId, jobFairBoothId);
        assignTask(jobFairId, jobFairBoothId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    private String applyApplication(CreateApplicationRequest request, String accountId, TestStatus testStatus, Double matchingPoint) {
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
        dto.setCreateDate(clock.millis());
        dto.setStatus(ApplicationStatus.DRAFT);
        dto.setOriginCvId(request.getCvId());
        dto.setBoothJobPositionDTO(regisDTO);
        dto.setTestStatus(testStatus);
        dto.setMatchingPoint(matchingPoint);
        //call create method
        ApplicationDTO result = applicationService.createNewApplication(dto);

        result = applicationService.submitApplication(result.getId(), accountId);

        //notification
        List<CompanyEmployeeDTO> employees = assignmentService.getAvailableInterviewer(result.getBoothJobPositionDTO().getJobFairBooth().getId());
        NotificationMessageDTO notificationMessage = NotificationMessageDTO.builder()
                .title(NotificationAction.APPLICATION.toString())
                .message(Jackson.getObjectMapper().writeValueAsString(result))
                .notificationType(NotificationType.NOTI)
                .build();
        List<String> ids = employees.stream().map(CompanyEmployeeDTO::getAccountId).collect(Collectors.toList());
        notificationService.createNotification(notificationMessage, ids);
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
        notificationMessageDTO.setTitle("Application finished evaluate");


        //send notification to attendant
        notificationService.createNotification(notificationMessageDTO, dto.getAttendant().getAccount().getId());
    }

    @PostMapping(ApiEndPoint.Demo.SUBMIT_MULTIPLE_APPLICATION + "/apply")
    public ResponseEntity<?> submitMultipleApplication(@RequestBody CreateApplicationAndEvaluateRequest request) {
        List<String> cvIdList = request.getCvId();
        final double HIGH_MATCHING_POINT = 0.8;
        final double MEDIUM_MATCHING_POINT = 0.6;
        final double LOW_MATCHING_POINT = 0.3;

        final double numberOfHighScore = cvIdList.size() * 0.5;
        final double numberOfMediumScore = cvIdList.size() * 0.3;
        final double numberOfLowScore = cvIdList.size() - numberOfHighScore - numberOfMediumScore;
        List<String> result = new ArrayList<>();

        AssignmentEntity assignmentEntity = assignmentRepository.findByEmployeeId(request.getEmployeeId(), request.getJobFairId()).get(1);

        String BoothJobPositionId = assignmentEntity.getJobFairBooth().getBoothJobPositions().get(0).getId();

        int i = 0;
        //High score
        for (; i < numberOfHighScore; i++) {
            String cv = cvIdList.get(i);
            String accountId = cvRepository.findById(cv).get().getAttendant().getAccountId();
            CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
            createApplicationRequest.setBoothJobPositionId(BoothJobPositionId);
            createApplicationRequest.setCvId(cv);
            double beta = ThreadLocalRandom.current().nextDouble(0, 0.1);
            String applicationId = applyApplication(createApplicationRequest, accountId, TestStatus.PASS, HIGH_MATCHING_POINT + beta);
            result.add(applicationId);
        }
        //Medium score
        for (; i < numberOfMediumScore + numberOfHighScore; i++) {
            String cv = cvIdList.get(i);
            String accountId = cvRepository.findById(cv).get().getAttendant().getAccountId();
            CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
            createApplicationRequest.setBoothJobPositionId(BoothJobPositionId);
            createApplicationRequest.setCvId(cv);
            double beta = ThreadLocalRandom.current().nextDouble(0, 0.1);
            String applicationId = applyApplication(createApplicationRequest, accountId, TestStatus.PASS, MEDIUM_MATCHING_POINT + beta);
            result.add(applicationId);
        }

        //Low score
        for (; i < numberOfLowScore + numberOfMediumScore + numberOfHighScore; i++) {
            String cv = cvIdList.get(i);
            String accountId = cvRepository.findById(cv).get().getAttendant().getAccountId();
            CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
            createApplicationRequest.setBoothJobPositionId(BoothJobPositionId);
            createApplicationRequest.setCvId(cv);
            double beta = ThreadLocalRandom.current().nextDouble(0, 0.1);
            String applicationId = applyApplication(createApplicationRequest, accountId, TestStatus.PASS, LOW_MATCHING_POINT + beta);
            result.add(applicationId);
        }
        return ResponseEntity.ok(result);
    }


    @PostMapping(ApiEndPoint.Demo.SUBMIT_MULTIPLE_APPLICATION + "/evaluate")
    public ResponseEntity<?> evaluateMultipleApplication(@RequestBody CreateApplicationAndEvaluateRequest request) {
        List<String> cvIdList = request.getCvId();

        final double numberOfApprove = cvIdList.size() * 0.3;
        final double numberOfPending = cvIdList.size() * 0.3;
        final double numberOfReject = cvIdList.size() - numberOfApprove - numberOfPending;

        Map<String, ApplicationStatus> result = new HashMap<>();


        int i = 0;
        //Approve
        for (; i < numberOfApprove; i++) {
            EvaluateApplicationRequest evaluateApplicationRequest = new EvaluateApplicationRequest();
            evaluateApplicationRequest.setApplicationId(cvIdList.get(i));
            evaluateApplicationRequest.setStatus(ApplicationStatus.APPROVE);
            evaluateApplicationRequest.setEvaluateMessage("Script auto evaluation approve");
            evaluateApplication(evaluateApplicationRequest, request.getEmployeeId());
            result.put(cvIdList.get(i), ApplicationStatus.APPROVE);
        }
        //Reject
        for (; i < numberOfApprove + numberOfReject; i++) {
            EvaluateApplicationRequest evaluateApplicationRequest = new EvaluateApplicationRequest();
            evaluateApplicationRequest.setApplicationId(cvIdList.get(i));
            evaluateApplicationRequest.setStatus(ApplicationStatus.REJECT);
            evaluateApplicationRequest.setEvaluateMessage("Script auto evaluation reject " + i);
            evaluateApplication(evaluateApplicationRequest, request.getEmployeeId());
            result.put(cvIdList.get(i), ApplicationStatus.REJECT);
        }
        return ResponseEntity.ok(result);
    }

    private String createCompanyWithEmployeeNums(int numberOfEmployees, String companyName) {
        CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest();
        createCompanyRequest.setCompanyDescription("description");
        createCompanyRequest.setCompanyEmail("_" + getSaltString(2) + companyName + "@gmail.com");
        createCompanyRequest.setAddress("address");
        createCompanyRequest.setTaxId(getSaltString(4));
        createCompanyRequest.setName(companyName);
        createCompanyRequest.setPhone(generatePhoneNumber());
        createCompanyRequest.setUrl("http://link.com");
        createCompanyRequest.setSizeId(1);
        CreateCompanyRequest.BenefitRequest benefit = new CreateCompanyRequest.BenefitRequest();
        benefit.setId(1);
        benefit.setDescription("alalala");
        List<CreateCompanyRequest.BenefitRequest> benefitss = new ArrayList<>();
        benefitss.add(benefit);
        List<Integer> subCategoriesIds = new ArrayList<>();
        createCompanyRequest.setBenefits(benefitss);
        createCompanyRequest.setSubCategoriesIds(subCategoriesIds);
        CompanyDTO companyDTO = companyMapper.toDTO(createCompanyRequest);
        companyDTO.setEmployeeMaxNum(5000);
        companyDTO = companyService.createCompany(companyDTO);
        CompanyEntity companyEntity = companyMapper.toEntity(companyDTO);

        //create company manager
        AccountDTO managerAccount = new AccountDTO();
        managerAccount.setEmail("manager_demo_company_" + companyName + "_" + getSaltString(2) + "@gmail.com");
        managerAccount.setPassword("123456");
        managerAccount.setPhone(generatePhoneNumber());
        managerAccount.setFirstname("Manager");
        managerAccount.setLastname("Of");
        managerAccount.setMiddlename(companyName);
        managerAccount.setGender(Gender.MALE);
        CompanyEmployeeDTO managerDTO = new CompanyEmployeeDTO();
        managerDTO.setAccount(managerAccount);
        managerDTO.setCompanyDTO(companyDTO);
        managerDTO.setDepartment("DEPARTMENT_DEMO_" + companyName);
        managerDTO.getAccount().setRole(Role.COMPANY_MANAGER);
        managerDTO.getAccount().setCreateTime(clock.millis());
        CompanyEmployeeEntity entity = companyEmployeeMapper.toEntity(managerDTO);
        entity.setCompany(companyEntity);
        AccountEntity accountEntity = entity.getAccount();
        accountEntity.setPassword(encoder.encode(accountEntity.getPassword()));
        accountEntity.setProfileImageUrl(AccountConstant.DEMO_DEFAULT_PROFILE_IMAGE_URL);
        accountEntity.setStatus(AccountStatus.VERIFIED);
        companyEmployeeRepository.save(entity);

        //create employees
        for (int i = 0; i < numberOfEmployees; i++) {
            CompanyEmployeeRegisterRequest createEmployeeRequest = new CompanyEmployeeRegisterRequest();
            createEmployeeRequest.setEmail("employee_demo_company_" + companyName + i + "_" + getSaltString(2) + "@gmail.com");
            createEmployeeRequest.setFirstName("Employee");
            createEmployeeRequest.setMiddleName("Of");
            createEmployeeRequest.setLastName(companyName + "_" + i);
            createEmployeeRequest.setDepartment("DEPARTMENT_DEMO_" + i + "OF_COMPANY_" + companyName);
            createEmployeeRequest.setEmployeeId("EMPLOYEE_" + i + "+_OF_COMPANY" + companyName);

            CompanyEmployeeDTO employeeDTO = companyEmployeeMapper.toDTO(createEmployeeRequest);
            String password = PasswordGenerator.generatePassword();
            employeeDTO.getAccount().setPassword(password);
            employeeDTO.getAccount().setRole(Role.COMPANY_EMPLOYEE);
            employeeDTO.getAccount().setStatus(AccountStatus.VERIFIED);
            employeeDTO.getAccount().setProfileImageUrl(AccountConstant.DEMO_DEFAULT_PROFILE_IMAGE_URL);
            employeeDTO.getAccount().setCreateTime(clock.millis());
            employeeDTO.getAccount().setGender(Gender.MALE);

            entity = companyEmployeeMapper.toEntity(employeeDTO);
            entity.setCompany(companyEntity);
            String hashPassword = encoder.encode(password);
            entity.getAccount().setPassword(hashPassword);
            companyEmployeeRepository.save(entity);
        }


        List<String> jobNames = Arrays.asList("Unity Game Developer", "Java Software Engineer", "Test Automation Specialist", "DevOps", "Project Manager", "Frontend Developer", "Full Stack Developer", "Accounting Associate", "Graphic Design Specialist");
        //create job position
        for (String name : jobNames){
            JobPositionDTO jobPositionDTO = new JobPositionDTO();
            jobPositionDTO.setTitle(name);
            jobPositionDTO.setContactPersonName(managerAccount.getFullname());
            jobPositionDTO.setContactEmail(managerAccount.getEmail());
            jobPositionDTO.setLanguage(Language.Vietnamese);
            jobPositionDTO.setLevel(JobLevel.FRESHER);
            jobPositionDTO.setJobType(JobType.INTERNSHIP);
            CompanyDTO newCompanyDTO = new CompanyDTO();
            newCompanyDTO.setId(companyDTO.getId());
            jobPositionDTO.setCompanyDTO(newCompanyDTO);
            jobPositionDTO.setDescription("This is the job description");
            jobPositionDTO.setRequirements("This is the job requirements");
            jobPositionDTO.setDescriptionKeyWord("developer");
            jobPositionDTO.setRequirementKeyWord("developer");
            long currentTime = clock.millis();
            jobPositionDTO.setCreatedDate(currentTime);

            JobPositionEntity jobPositionEntity = jobPositionMapper.toEntity(jobPositionDTO);
            jobPositionRepository.save(jobPositionEntity);
        }



        return companyDTO.getId();
    }

    @PostMapping(ApiEndPoint.Demo.CREATE_COMPANIES_WITH_80_EMPLOYEES)
    public ResponseEntity<?> createCompaniesAndEmployees(@RequestParam int numberOfEmployees, @RequestParam String companyName) {
        String companyId = createCompanyWithEmployeeNums(numberOfEmployees, companyName);
        Map<String, String> body = new HashMap<>();
        body.put("id", companyId);
        return ResponseEntity.ok(body);
    }

    private JobFairDTO createDraftJobFair(String companyId, boolean isAssignment) {
        LocalDate localDate = LocalDate.now();
        LocalDate fiveDayAgo = localDate.minusDays(5);
        LocalDate twoDayAgo = localDate.minusDays(2);
        LocalDate oneDayAgo = localDate.minusDays(1);
        LocalDate twoDayLater = localDate.plusDays(2);
        LocalDate threeDayLater = localDate.plusDays(3);
        LocalDate fiveDayLater = localDate.plusDays(5);


        LocalDateTime startOfDay = fiveDayAgo.atTime(LocalTime.MIN);
        long decorateStartTime = startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        LocalDateTime endOfDay = twoDayAgo.atTime(LocalTime.MAX);
        long decorateEndTime = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        startOfDay = oneDayAgo.atTime(LocalTime.MIN);
        long publicStartTime = startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        endOfDay = fiveDayLater.atTime(LocalTime.MAX);
        long publicEndTime = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        DraftJobFairRequest request = new DraftJobFairRequest();
        request.setName("JOB_FAIR_DEMO_" + getSaltString(2));

        request.setDecorateStartTime(decorateStartTime);
        request.setDecorateEndTime(decorateEndTime);
        request.setPublicStartTime(publicStartTime);
        request.setPublicEndTime(publicEndTime);
        request.setDescription("description about job fair");
        request.setTargetAttendant("Sinh vien FPT");
        request.setHostName("Demo");
        JobFairDTO jobFairDTO = jobFairMapper.toDTO(request);
        jobFairDTO.setThumbnailUrl("https://d1t63ajhfi2lx8.cloudfront.net/JobFair-thumbnail/9a5dbbe4-b2b6-4075-9221-a7986d3b49d6");
        jobFairDTO.setCompany(CompanyDTO.builder().id(companyId).build());

        ShiftDTO morningShift = new ShiftDTO();
        morningShift.setBeginTime(25200000L);
        morningShift.setEndTime(36000000L);
        morningShift.setJobFairId(jobFairDTO.getId());

        ShiftDTO afternoonShift = new ShiftDTO();
        afternoonShift.setBeginTime(43200000L);
        afternoonShift.setEndTime(54000000L);
        afternoonShift.setJobFairId(jobFairDTO.getId());

        jobFairDTO.setShifts(new ArrayList<>());
        jobFairDTO.getShifts().add(morningShift);
        jobFairDTO.getShifts().add(afternoonShift);

        jobFairDTO = jobFairService.createNewJobFair(jobFairDTO);

        layoutService.pickJobFairLayout(jobFairDTO.getId(), "ca8979fb-07de-448c-9da1-cd1f485a4255", companyId);

        if (!isAssignment) return jobFairDTO;
        /*
         * 1. Random total of booths that need assign
         * 2. Calculate total number of employee that need to be assigned: totalOfBooth * 4
         * 3. Use for i to assign  employee. In this loop, get first 4 employee from total of employee above, then assign equally to each booth
         * */
        List<JobFairBoothDTO> jobFairBooths = jobFairBoothService.getCompanyBoothByJobFairId(jobFairDTO.getId());

        final int minimumBooth = 1;
        final int maximumBooth = 5;

        final int totalNumberOfAssignBooth = ThreadLocalRandom.current().nextInt(minimumBooth, maximumBooth + 1);

        //maximum needed employee will be 40, so get 45
        Page<CompanyEmployeeDTO> pageResult = companyEmployeeService.getAllCompanyEmployees(companyId, "", 45, 0, "account.createTime", Sort.Direction.ASC);

        List<CompanyEmployeeDTO> listShuffle = new ArrayList<>(pageResult.getContent());
        Collections.shuffle(listShuffle);

        List<CompanyEmployeeEntity> managers = companyEmployeeRepository.findAllByCompanyIdAndAccountRoleId(companyId, Role.COMPANY_MANAGER.ordinal());
        CompanyEmployeeEntity manager = managers.get(0);

        for (int i = 0; i <= totalNumberOfAssignBooth; i++) {

            List<CompanyEmployeeDTO> neededEmployeeList = listShuffle
                    .stream()
                    .filter(item -> item.getAccount().getRole() != Role.COMPANY_MANAGER)
                    .limit(4).collect(Collectors.toList());

            //make sure these employees must be VERIFIED
            neededEmployeeList.forEach(item -> {
                AccountDTO dto = item.getAccount();
                dto.setStatus(AccountStatus.VERIFIED);
                item.setAccount(dto);
            });

            //get first 4 employees from total needed employee
            //Employee 1: SUPERVISOR
            assignmentService
                    .assignEmployee(
                            manager.getAccountId(),
                            neededEmployeeList.get(0).getAccountId(),
                            jobFairBooths.get(i).getId(),
                            AssignmentType.SUPERVISOR,
                            companyId,
                            decorateStartTime,
                            publicEndTime);
            //Employee 2: DECORATOR
            assignmentService
                    .assignEmployee(
                            manager.getAccountId(),
                            neededEmployeeList.get(1).getAccountId(),
                            jobFairBooths.get(i).getId(),
                            AssignmentType.DECORATOR,
                            companyId,
                            decorateStartTime,
                            decorateEndTime
                    );
            //Employee 3: STAFF
            assignmentService
                    .assignEmployee(
                            manager.getAccountId(),
                            neededEmployeeList.get(2).getAccountId(),
                            jobFairBooths.get(i).getId(),
                            AssignmentType.STAFF,
                            companyId,
                            decorateStartTime,
                            decorateEndTime
                    );
            //Employee 4: STAFF
            assignmentService
                    .assignEmployee(
                            manager.getAccountId(),
                            neededEmployeeList.get(3).getAccountId(),
                            jobFairBooths.get(i).getId(),
                            AssignmentType.STAFF,
                            companyId,
                            decorateStartTime,
                            decorateEndTime
                    );

            listShuffle.removeAll(neededEmployeeList);
        }
        return jobFairDTO;

    }

    protected void decorateJobFair(String jobFairId){
        Random rand = new Random();
        List<String> defaultBoothUrls = Arrays.asList("https://d1t63ajhfi2lx8.cloudfront.net/Default/default_booth.glb", "https://d1t63ajhfi2lx8.cloudfront.net/Default/default2.glb", "https://d1t63ajhfi2lx8.cloudfront.net/Default/default3.glb", "https://d1t63ajhfi2lx8.cloudfront.net/Default/default4.glb");

        JobFairEntity jobFair = jobFairRepository.getById(jobFairId);
        long oldDecorateEndTime = jobFair.getDecorateEndTime();
        long oldDecorateStartTime = jobFair.getDecorateStartTime();
        jobFair.setDecorateStartTime(0L);
        jobFair.setDecorateEndTime(clock.millis() + 10000000);
        jobFairRepository.saveAndFlush(jobFair);

        //get booths that have employee assign to
        List<BoothJobPositionDTO> jobPositionDTOS = createBoothJobPosition(jobFairId);



        //make latest booth layout, assign to booth
        JobFairProgressDTO jobFairProgressDTO = jobFairService.getJobFairProgress(jobFairId);

        for (JobFairProgressDTO.Booth booth: jobFairProgressDTO.getBooths()){
            JobFairBoothEntity jobFairBooth = jobFairBoothRepository.getById(booth.getId());
            jobFairBooth.setName("Department");
            JobFairBoothLayoutEntity layout = new JobFairBoothLayoutEntity();
            layout.setId(UUID.randomUUID().toString());
            layout.setVersion(1);
            layout.setCreateDate(clock.millis());
            String url = defaultBoothUrls.get(rand.nextInt(defaultBoothUrls.size()));
            System.out.println(url);
            layout.setUrl(url);
            layout.setJobFairBooth(jobFairBooth);
            jobFairBoothLayoutRepository.save(layout);
            jobFairBoothRepository.save(jobFairBooth);
        }

        jobFair.setDecorateEndTime(oldDecorateEndTime);
        jobFair.setDecorateStartTime(oldDecorateStartTime);
        jobFairRepository.saveAndFlush(jobFair);
    }

    @Transactional
    protected void publishFakeJobFair(String jobFairId) {
        //publish job fair
        JobFairEntity jobFair = jobFairRepository.getById(jobFairId);
        jobFair.setStatus(JobFairPlanStatus.PUBLISH);
        jobFairRepository.saveAndFlush(jobFair);
    }

    @PostMapping(ApiEndPoint.Demo.CREATE_1_DRAFT_JOB_FAIR_FOR_COMPANY)
    public ResponseEntity<?> create1DraftForEachCompany(@RequestParam String companyId) {
        JobFairDTO jobFairDTO = createDraftJobFair(companyId, true);
        return ResponseEntity.ok(jobFairDTO);
    }

    @PostMapping(ApiEndPoint.Demo.CREATE_JOB_FAIR_DRAFT_WITHOUT_ASSIGNMENT)
    public ResponseEntity<?> create1DraftWithoutAssignment(@RequestParam String companyId) {
        JobFairDTO jobFairDTO = createDraftJobFair(companyId, false);
        return ResponseEntity.ok(jobFairDTO);
    }

    @PostMapping(ApiEndPoint.Demo.PASS_TEST)
    @Transactional
    public ResponseEntity<?> passTestForUser(@RequestParam String attendantId) {
        List<ApplicationEntity> applicationEntities = applicationRepository.findByAttendantAccountId(attendantId);
        applicationEntities.forEach(applicationEntity -> {
            applicationEntity.setTestStatus(TestStatus.PASS);
        });
        applicationRepository.saveAll(applicationEntities);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiEndPoint.Demo.PUBLISH_FAKE_JOB_FAIR)
    public ResponseEntity<?> publishFakeJobFairs(@RequestParam String jobFairId) {
        publishFakeJobFair(jobFairId);
        decorateJobFair(jobFairId);
        return ResponseEntity.ok().build();
    }
}
