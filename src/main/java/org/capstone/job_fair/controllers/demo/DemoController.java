package org.capstone.job_fair.controllers.demo;

import com.amazonaws.Response;
import com.opencsv.CSVWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.ApplicationConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.account.cv.CreateApplicationRequest;
import org.capstone.job_fair.controllers.payload.requests.attendant.EvaluateApplicationRequest;
import org.capstone.job_fair.controllers.payload.requests.attendant.RegisterAttendantRequest;
import org.capstone.job_fair.controllers.payload.requests.company.CompanyEmployeeRegisterRequest;
import org.capstone.job_fair.controllers.payload.requests.company.CreateQuestionsRequest;
import org.capstone.job_fair.controllers.payload.requests.demo.CreateApplicationAndEvaluateRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.application.ApplicationDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.CvDTO;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.dtos.company.job.questions.ChoicesDTO;
import org.capstone.job_fair.models.dtos.company.job.questions.QuestionsDTO;
import org.capstone.job_fair.models.dtos.dynamoDB.NotificationMessageDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.BoothJobPositionDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothDTO;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.AssignmentEntity;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.models.enums.NotificationType;
import org.capstone.job_fair.models.enums.TestStatus;
import org.capstone.job_fair.repositories.attendant.cv.CvRepository;
import org.capstone.job_fair.repositories.job_fair.JobFairRepository;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.AssignmentRepository;
import org.capstone.job_fair.repositories.job_fair.job_fair_booth.BoothJobPositionRepository;
import org.capstone.job_fair.services.interfaces.attendant.AttendantService;
import org.capstone.job_fair.services.interfaces.attendant.application.ApplicationService;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.interfaces.company.job.JobPositionService;
import org.capstone.job_fair.services.interfaces.company.job.question.QuestionsService;
import org.capstone.job_fair.services.interfaces.job_fair.booth.JobFairBoothService;
import org.capstone.job_fair.services.interfaces.matching_point.MatchingPointService;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.capstone.job_fair.services.mappers.company.CompanyEmployeeMapper;
import org.capstone.job_fair.services.mappers.company.job.question.QuestionsMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
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

    @Autowired
    private BoothJobPositionRepository boothJobPositionRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private CompanyEmployeeService companyEmployeeService;

    @Autowired
    private CompanyEmployeeMapper companyEmployeeMapper;

    @Autowired
    private QuestionsService questionsService;

    @Autowired
    private QuestionsMapper questionsMapper;




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

    //This function must be call when Supervisor edit booth profile. No add booth job position manually and must run script
    //REMEMBER: run script to delete all application first !
    private List<BoothJobPositionDTO> createBoothJobPosition(String jobFairId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //get 5 job positions first
        Page<JobPositionDTO> jobPositions = jobPositionService.getAllJobPositionOfCompany(userDetails.getCompanyId(), null, null, null, 5, 0, "createdDate", Sort.Direction.ASC);

        //Add 1 question (includes 1 correct answer and 1 wrong answer) to each job position
        jobPositions.getContent().stream().forEach(item -> {
            CreateQuestionsRequest request = new CreateQuestionsRequest();
            List<CreateQuestionsRequest.Choice> listChoices = new ArrayList<>();
            //correct choice
            CreateQuestionsRequest.Choice correctChoice = new CreateQuestionsRequest.Choice();
            correctChoice.setContent("Answer: câu này đúng nè chọn đi");
            correctChoice.setIsCorrect(true);
            listChoices.add(correctChoice);

            //wrong choice
            CreateQuestionsRequest.Choice wrongChoice = new CreateQuestionsRequest.Choice();
            wrongChoice.setContent("Answer: câu này sai đó đừng chọn");
            wrongChoice.setIsCorrect(false);
            listChoices.add(wrongChoice);

            request.setJobPositionId(item.getId());
            request.setContent("Question: Hãy tìm câu trả lời đúng");
            request.setChoicesList(listChoices);

            //create questions
            createQuestions(request);
        });

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
                BoothJobPositionDTO boothJobPosition = BoothJobPositionDTO
                        .builder()
                        .originJobPosition(item.getId())
                        .minSalary(Double.parseDouble("5"))
                        .maxSalary(Double.parseDouble("5"))
                        .numOfPosition(Integer.parseInt("1"))
                        .isHaveTest(true)
                        .note("this job position required test")
                        .testTimeLength(Integer.parseInt("15"))
                        .numOfQuestion(Integer.parseInt("2"))
                        .passMark(Double.parseDouble("1"))
                        .jobFairBooth(jobFairBoothDto)
                        .descriptionKeyWord("abc").requirementKeyWord("abc").build();
                return boothJobPosition;
            }).collect(Collectors.toList());
            dto.setBoothJobPositions(boothJobPositions);
            jobFairBoothService.updateJobFairBooth(dto, userDetails.getCompanyId());
        }


        List<BoothJobPositionDTO> result = new ArrayList<>();
        jobFairBoothService.getCompanyBoothByJobFairId(jobFairId).stream().forEach(item -> item.getBoothJobPositions().stream().forEach(element -> result.add(element)));
        return result;
    }

    private void createQuestions(CreateQuestionsRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QuestionsDTO questionsDTO = new QuestionsDTO();
        JobPositionDTO jobPositionDTO = new JobPositionDTO();
        jobPositionDTO.setId(request.getJobPositionId());
        questionsDTO.setJobPosition(jobPositionDTO);
        questionsDTO.setContent(request.getContent());
        List<ChoicesDTO> choicesDTOList = request.getChoicesList().stream().map(choice -> new ChoicesDTO(null, choice.getContent(), choice.getIsCorrect(), null)).collect(Collectors.toList());
        questionsDTO.setChoicesList(choicesDTOList);
        questionsService.createQuestion(questionsDTO, userDetails.getId(), userDetails.getCompanyId());
    }


    private List<String> createAttendantsWithOneCv(Integer numberOfAttendants) {

        String attendantEmail = null;
        String attendantId = null;

        List<String> result = new ArrayList<>();

        for (int i = 0; i < numberOfAttendants; i++) {
            Map<String, String> map = new HashMap<String, String>();
            //Create attendant
            attendantEmail = createAttendant(i).split(",")[0];
            attendantId = createAttendant(i).split(",")[1];

            //Create CV
            String cvId = createCV(attendantId);
            result.add(cvId);
        }
        return result;

    }


    @GetMapping(ApiEndPoint.Demo.CREATE_ATTENDANT)
    public ResponseEntity<?> createAttendantWithCV(@RequestParam Integer numberOfAttendants) {
        return ResponseEntity.ok(createAttendantsWithOneCv(numberOfAttendants));
    }

    @GetMapping(ApiEndPoint.Demo.PUBLISH_JOBFAIR)
    public ResponseEntity<?> publishJobFair(@RequestParam String JobFairId) {
        JobFairEntity jobFair = jobFairRepository.findById(JobFairId).get();
        jobFair.setPublicStartTime(jobFair.getCreateTime());
        jobFairRepository.save(jobFair);
        return ResponseEntity.ok().build();
    }

    @GetMapping(ApiEndPoint.Demo.CREATE_BOOTH_JOB_POSITION)
    public ResponseEntity<?> createBoothJobPositions(@RequestParam String jobFairId) {
        return ResponseEntity.ok(createBoothJobPosition(jobFairId));
    }

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
        dto.setCreateDate(new Date().getTime());
        dto.setStatus(ApplicationStatus.DRAFT);
        dto.setOriginCvId(request.getCvId());
        dto.setBoothJobPositionDTO(regisDTO);
        dto.setTestStatus(testStatus);
        dto.setMatchingPoint(matchingPoint);
        //call create method
        ApplicationDTO result = applicationService.createNewApplication(dto);

//        matchingPointService.calculateFromApplication(result.getId()).subscribe().dispose();
        applicationService.submitApplication(result.getId(), accountId);
        return result.getId();
    }

    private void evaluateApplication(EvaluateApplicationRequest request, String userId) {
        final double HIGH_MATCHING_POINT = 70;
        final double MEDIUM_MATCHING_POINT = 50;
        final double LOW_MATCHING_POINT = 50;

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

    @PostMapping(ApiEndPoint.Demo.SUBMIT_MULTIPLE_APPLICATION + "/apply")
    public ResponseEntity<?> submitMultipleApplication(@RequestBody CreateApplicationAndEvaluateRequest request) {
        List<String> cvIdList = request.getCvId();

        if (cvIdList.size() <= 15) {
            return GenericResponse.build("Cv list must be >= 15 CVs", HttpStatus.NOT_FOUND);
        }

        final double HIGH_MATCHING_POINT = 0.7;
        final double MEDIUM_MATCHING_POINT = 0.5;
        final double LOW_MATCHING_POINT = 0.3;

        final double numberOfFailTest = cvIdList.size() * 0.5;
        final double numberOfPassTest = cvIdList.size() * 0.5;
        final double numberOfApprove = numberOfPassTest * 0.3;
        final double numberOfPending = numberOfPassTest * 0.3;
        final double numberOfReject = numberOfPassTest - numberOfApprove - numberOfPending;
        List<String> result = new ArrayList<>();

        AssignmentEntity assignmentEntity = assignmentRepository.findByEmployeeId(request.getEmployeeId(), request.getJobFairId()).get(1);

        String BoothJobPositionId = assignmentEntity.getJobFairBooth().getBoothJobPositions().get(0).getId();

        int i = 0;
        //Approve
        for (; i < numberOfApprove; i++) {
            String cv = cvIdList.get(i);
            String accountId = cvRepository.findById(cv).get().getAttendant().getAccountId();
            CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
            createApplicationRequest.setBoothJobPositionId(BoothJobPositionId);
            createApplicationRequest.setCvId(cv);
            String applicationId = applyApplication(createApplicationRequest, accountId, TestStatus.PASS, HIGH_MATCHING_POINT);
            result.add(applicationId);
        }
        //Reject
        for (; i < numberOfApprove + numberOfReject; i++) {
            String cv = cvIdList.get(i);
            String accountId = cvRepository.findById(cv).get().getAttendant().getAccountId();
            CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
            createApplicationRequest.setBoothJobPositionId(BoothJobPositionId);
            createApplicationRequest.setCvId(cv);
            String applicationId = applyApplication(createApplicationRequest, accountId, TestStatus.PASS, MEDIUM_MATCHING_POINT);
            result.add(applicationId);
        }

        //Pending
        for (; i < numberOfApprove + numberOfReject + numberOfPending; i++) {
            String cv = cvIdList.get(i);
            String accountId = cvRepository.findById(cv).get().getAttendant().getAccountId();
            CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
            createApplicationRequest.setBoothJobPositionId(BoothJobPositionId);
            createApplicationRequest.setCvId(cv);
            String applicationId = applyApplication(createApplicationRequest, accountId, TestStatus.PASS, LOW_MATCHING_POINT);
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
            System.out.println(i);
            EvaluateApplicationRequest evaluateApplicationRequest = new EvaluateApplicationRequest();
            evaluateApplicationRequest.setApplicationId(cvIdList.get(i));
            evaluateApplicationRequest.setStatus(ApplicationStatus.REJECT);
            evaluateApplicationRequest.setEvaluateMessage("Script auto evaluation reject " + i);
            System.out.println("reject");
            evaluateApplication(evaluateApplicationRequest, request.getEmployeeId());
            result.put(cvIdList.get(i), ApplicationStatus.REJECT);
        }
        return ResponseEntity.ok(result);
    }
    private List<CompanyEmployeeDTO> createMultipleEmployee(int numberOfEmployees) {
        List<CompanyEmployeeDTO> result = new ArrayList<>();
        for (int i = 0; i < numberOfEmployees; i++) {
            CompanyEmployeeRegisterRequest request = new CompanyEmployeeRegisterRequest();
            request.setEmail("demo_employee" + i + "@gmail.com");
            request.setEmployeeId("DEMO_"+i);
            request.setDepartment("DEMO_DEPARTMENT");
            request.setFirstName("Nguyen ");
            request.setMiddleName("Van ");
            request.setLastName("A " + i);
            CompanyEmployeeDTO dto = companyEmployeeMapper.toDTO(request);
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String companyId = userDetails.getCompanyId();
            CompanyDTO companyDTO = new CompanyDTO();
            companyDTO.setId(companyId);
            dto.setCompanyDTO(companyDTO);
            CompanyEmployeeDTO resultDTO = companyEmployeeService.createNewCompanyEmployeeAccount(dto);
            result.add(resultDTO);
        }

        return result;
    }

    @PostMapping(ApiEndPoint.Demo.CREATE_EMPLOYEES)
    public ResponseEntity<?> createEmployees(int numberOfEmployees) {
        return ResponseEntity.ok(createMultipleEmployee(numberOfEmployees));
    }

}
