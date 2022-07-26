package org.capstone.job_fair.controllers.demo;

import com.amazonaws.util.json.Jackson;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.account.cv.CreateApplicationRequest;
import org.capstone.job_fair.controllers.payload.requests.attendant.EvaluateApplicationRequest;
import org.capstone.job_fair.controllers.payload.requests.attendant.RegisterAttendantRequest;
import org.capstone.job_fair.controllers.payload.requests.company.CompanyEmployeeRegisterRequest;
import org.capstone.job_fair.controllers.payload.requests.company.CreateCompanyRequest;
import org.capstone.job_fair.controllers.payload.requests.company.CreateQuestionsRequest;
import org.capstone.job_fair.controllers.payload.requests.demo.CreateApplicationAndEvaluateRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.DraftJobFairRequest;
import org.capstone.job_fair.controllers.payload.responses.CompanyEmployeeResponse;
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
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.dtos.job_fair.LayoutBoothDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.AssignmentDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.BoothJobPositionDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothDTO;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.entities.job_fair.JobFairEntity;
import org.capstone.job_fair.models.entities.job_fair.booth.AssignmentEntity;
import org.capstone.job_fair.models.enums.*;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.repositories.attendant.cv.CvRepository;
import org.capstone.job_fair.repositories.company.CompanyEmployeeRepository;
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
import org.capstone.job_fair.services.interfaces.job_fair.JobFairService;
import org.capstone.job_fair.services.interfaces.job_fair.LayoutService;
import org.capstone.job_fair.services.interfaces.job_fair.booth.AssignmentService;
import org.capstone.job_fair.services.interfaces.job_fair.booth.JobFairBoothService;
import org.capstone.job_fair.services.interfaces.matching_point.MatchingPointService;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.capstone.job_fair.services.mappers.company.CompanyEmployeeMapper;
import org.capstone.job_fair.services.mappers.company.CompanyMapper;
import org.capstone.job_fair.services.mappers.company.job.question.QuestionsMapper;
import org.capstone.job_fair.services.mappers.job_fair.JobFairMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    private JobPositionService jobPositionService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private MatchingPointService matchingPointService;

    @Autowired
    private JobFairRepository jobFairRepository;

    @Autowired
    @Qualifier("LocalNotificationService")
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
    private JobFairBoothService boothService;

    @Autowired
    private CompanyEmployeeRepository employeeRepository;

    @Autowired
    private LayoutService layoutService;


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

    private CreateQuestionsRequest.Choice generateAnswer(String content, Boolean status) {
        return new CreateQuestionsRequest.Choice(content, status);
    }

    private List<CreateQuestionsRequest.Choice> createTrueFalseQuestion(String trueAnswer, String falseAnswer) {
        List<CreateQuestionsRequest.Choice> listChoices = new ArrayList<>();

        CreateQuestionsRequest.Choice choice1 = new CreateQuestionsRequest.Choice();
        choice1.setContent(trueAnswer);
        choice1.setIsCorrect(true);
        listChoices.add(choice1);

        CreateQuestionsRequest.Choice choice2 = new CreateQuestionsRequest.Choice();
        choice2.setContent(falseAnswer);
        choice2.setIsCorrect(false);
        listChoices.add(choice2);

        return listChoices;
    }

    private List<CreateQuestionsRequest.Choice> createMultipleChoiceQuestion(List<String> wrongAnswer, List<String> correctAnswer) {
        List<CreateQuestionsRequest.Choice> listChoices = new ArrayList<>();

        wrongAnswer.forEach(item -> {
            CreateQuestionsRequest.Choice choice = new CreateQuestionsRequest.Choice();
            choice.setContent(item);
            choice.setIsCorrect(false);
            listChoices.add(choice);
        });

        correctAnswer.forEach(item -> {
            CreateQuestionsRequest.Choice choice = new CreateQuestionsRequest.Choice();
            choice.setContent(item);
            choice.setIsCorrect(true);
            listChoices.add(choice);
        });

        return listChoices;
    }

    private List<CreateQuestionsRequest> generateQuestion(String jobPositionId) {
        //create 3 True False questions
        List<CreateQuestionsRequest.Choice> TF_Question1 = createTrueFalseQuestion("T-In the cloud, instead of physically managing operation system, you use services.", "F-In the cloud, instead of physically managing middleware, you use services.");
        List<CreateQuestionsRequest.Choice> TF_Question2 = createTrueFalseQuestion("T-Enable MFA for the root user", "F-Using the root user for routine administrative tasks");
        List<CreateQuestionsRequest.Choice> TF_Question3 = createTrueFalseQuestion("T-CloudWatch", "F-CloudFront");

        //create 3 multiple choice questions
        List<String> wrongChoice_1 = new ArrayList<>();
        wrongChoice_1.add("Allows all inbound traffic and blocks all outbound traffic.");
        wrongChoice_1.add("Allows all inbound and outbound traffic");
        wrongChoice_1.add("Blocks all inbound and outbound traffic.");
        List<String> correctChoice_1 = new ArrayList<>();
        correctChoice_1.add("Blocks all inbound traffic and allows all outbound traffic (true)");
        List<CreateQuestionsRequest.Choice> MC_Question1 = createMultipleChoiceQuestion(wrongChoice_1, correctChoice_1);

        List<String> wrongChoice_2 = new ArrayList<>();
        wrongChoice_2.add("Instance placement and instance size");
        wrongChoice_2.add("Instance AMI and networking speed");
        wrongChoice_2.add("Instance tenancy and instance billing");
        List<String> correctChoice_2 = new ArrayList<>();
        correctChoice_2.add("Instance family and instance size (true)");
        List<CreateQuestionsRequest.Choice> MC_Question2 = createMultipleChoiceQuestion(wrongChoice_2, correctChoice_2);

        List<String> wrongChoice_3 = new ArrayList<>();
        wrongChoice_3.add("Amaztrong");
        wrongChoice_3.add("Amazon Web Identity and Access Encryption");
        wrongChoice_3.add("Amazon Elastic 3");
        List<String> correctChoice_3 = new ArrayList<>();
        correctChoice_1.add("Amazon Elastic Block Storage Encryption (true)");
        List<CreateQuestionsRequest.Choice> MC_Question3 = createMultipleChoiceQuestion(wrongChoice_3, correctChoice_3);
///////////////////////////////
        List<CreateQuestionsRequest> result = new ArrayList<>();
        //TRUE-FALSE Questions
        CreateQuestionsRequest TF1 = new CreateQuestionsRequest();
        TF1.setContent("Which of the following pieces of information do you need to create a Virtual Private Cloud (VPC)?");
        TF1.setChoicesList(TF_Question1);
        TF1.setJobPositionId(jobPositionId);

        CreateQuestionsRequest TF2 = new CreateQuestionsRequest();
        TF2.setContent("What must you do to allow resources in a public subnet to communicate with the internet?");
        TF2.setChoicesList(TF_Question2);
        TF2.setJobPositionId(jobPositionId);

        CreateQuestionsRequest TF3 = new CreateQuestionsRequest();
        TF3.setContent("Which amongst the following tools is for monitoring?");
        TF3.setChoicesList(TF_Question3);
        TF3.setJobPositionId(jobPositionId);

        result.add(TF1);
        result.add(TF2);
        result.add(TF3);

        //MC_Questions
        CreateQuestionsRequest MC1 = new CreateQuestionsRequest();
        MC1.setContent("Which of the following is true for the default settings of a security group?");
        MC1.setChoicesList(MC_Question1);
        MC1.setJobPositionId(jobPositionId);

        CreateQuestionsRequest MC2 = new CreateQuestionsRequest();
        MC2.setContent("What does an Amazon EC2 instance type indicate?");
        MC2.setChoicesList(MC_Question2);
        MC2.setJobPositionId(jobPositionId);

        CreateQuestionsRequest MC3 = new CreateQuestionsRequest();
        MC3.setContent("Which of the following is Amazon Web Services' latest encryption tool?");
        MC3.setChoicesList(MC_Question3);
        MC3.setJobPositionId(jobPositionId);

        result.add(MC1);
        result.add(MC2);
        result.add(MC3);

        return result;
    }

    //This function must be call when Supervisor edit booth profile. No add booth job position manually and must run script
    //REMEMBER: run script to delete all application first !
    private List<BoothJobPositionDTO> createBoothJobPosition(String jobFairId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //get 5 job positions first
        Page<JobPositionDTO> jobPositions = jobPositionService.getAllJobPositionOfCompany(userDetails.getCompanyId(), null, null, null, 5, 0, "createdDate", Sort.Direction.ASC);

        //Add 1 question (includes 1 correct answer and 1 wrong answer) to each job position
        jobPositions.getContent().stream().forEach(item -> {
            List<CreateQuestionsRequest> requestList = generateQuestion(item.getId());
            requestList.forEach(request -> {
                //create questions
                createQuestions(request);
            });
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
                        .numOfQuestion(Integer.parseInt("4"))
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
        dto.setCreateDate(new Date().getTime());
        dto.setStatus(ApplicationStatus.DRAFT);
        dto.setOriginCvId(request.getCvId());
        dto.setBoothJobPositionDTO(regisDTO);
        dto.setTestStatus(testStatus);
        dto.setMatchingPoint(matchingPoint);
        //call create method
        ApplicationDTO result = applicationService.createNewApplication(dto);

//        matchingPointService.calculateFromApplication(result.getId()).subscribe().dispose();
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
            request.setEmployeeId("DEMO_" + i);
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

    private List<String> createMultipleCompanies(int numberOfCompanies, int numberOfEmployees) {
        List<String> companyIds = new ArrayList<>();
        for (int i = 0; i <= numberOfCompanies; i++) {
            CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest();
            createCompanyRequest.setCompanyDescription("description");
            createCompanyRequest.setCompanyEmail("demo_email_company" + i + "@gmail.com");
            createCompanyRequest.setAddress("address");
            createCompanyRequest.setTaxId("taxID0" + i);
            createCompanyRequest.setName("Auto_Company_0" + i);
            createCompanyRequest.setPhone("0123456789");
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
            CompanyDTO dto = companyMapper.toDTO(createCompanyRequest);
            dto = companyService.createCompany(dto);

            //create company manager
            AccountDTO managerAccount = new AccountDTO();
            managerAccount.setEmail("manager_demo_company" + i + "@gmail.com");
            managerAccount.setPassword("123456");
            managerAccount.setPhone("0123456789");
            managerAccount.setFirstname("Manager");
            managerAccount.setLastname("Of");
            managerAccount.setMiddlename("Company Demo " + i);
            //set default gender of company manager is MALE
            managerAccount.setGender(Gender.MALE);
            CompanyEmployeeDTO managerDTO = new CompanyEmployeeDTO();
            managerDTO.setAccount(managerAccount);
            managerDTO.setCompanyDTO(dto);
            managerDTO.setDepartment("DEPARTMENT_DEMO_" + i);
            companyEmployeeService.createNewCompanyManagerAccount(managerDTO);

            //create 80 employees
            for (int t = 0; t <= numberOfEmployees; t++) {
                CompanyEmployeeRegisterRequest createEmployeeRequest = new CompanyEmployeeRegisterRequest();
                createEmployeeRequest.setEmail("employee_demo_company_0" + i + t + "@gmail.com");
                createEmployeeRequest.setFirstName("Pham");
                createEmployeeRequest.setMiddleName(" Cao");
                createEmployeeRequest.setLastName(" Son_0" + i + t);
                createEmployeeRequest.setDepartment("DEPARTMENT_DEMO_" + t + "OF_COMPANY_" + i);
                createEmployeeRequest.setEmployeeId("EMPLOYEE_" + t + "+_OF_COMPANY" + i);

                CompanyEmployeeDTO employeeDTO = companyEmployeeMapper.toDTO(createEmployeeRequest);
                employeeDTO.setCompanyDTO(dto);
                companyEmployeeService.createNewCompanyEmployeeAccount(employeeDTO);

            }
            companyIds.add(dto.getId());
        }
        return companyIds;
    }

    @PostMapping(ApiEndPoint.Demo.CREATE_COMPANIES_WITH_80_EMPLOYEES)
    public ResponseEntity<?> createCompaniesAnd80Employees(@RequestParam int numOfCompanies, @RequestParam int numberOfEmployees) {
        return ResponseEntity.ok(createMultipleCompanies(numOfCompanies, numberOfEmployees));
    }

    @PostMapping(ApiEndPoint.Demo.CREATE_JOB_FAIR_DRAFT_FOR_COMPANIES)
    public ResponseEntity<?> create4DraftJobFairForCompany() {
        List<JobFairDTO> result = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            DraftJobFairRequest request = new DraftJobFairRequest();
            request.setName("CREATED_JOB_FAIR_DRAFT_" + i);
            request.setDecorateStartTime(1658730607000L);
            request.setDecorateEndTime(1661409007000L);
            request.setPublicStartTime(1661754607000L);
            request.setPublicEndTime(1667025007000L);
            request.setDescription("description about job fair");
            request.setTargetAttendant("Sinh vien FPT");
            request.setHostName("Demo haha");
            JobFairDTO jobFairDTO = jobFairMapper.toDTO(request);
            jobFairDTO.setThumbnailUrl("https://d3polnwtp0nqe6.cloudfront.net/JobFair-thumbnail/6b9f083b-9a95-4bd3-b264-4822ae76b8f6");


            jobFairDTO.setCompany(CompanyDTO.builder().id(userDetails.getCompanyId()).build());
            jobFairDTO = jobFairService.createNewJobFair(jobFairDTO);
            layoutService.pickJobFairLayout(jobFairDTO.getId(), "b40ab83c-8f13-44ea-91b7-993f2263efae", userDetails.getCompanyId());


            /*
             * 1. Random total of booths that need assign
             * 2. Calculate total number of employee that need to be assigned: totalOfBooth * 4
             * 3. Use for i to assign  employee. In this loop, get first 4 employee from total of emmployee above, then assign equally to the each booth
             * */
            List<JobFairBoothDTO> jobFairBooths = jobFairBoothService.getCompanyBoothByJobFairId(jobFairDTO.getId());
            final Integer minimumBooth = 1;
            final Integer maximumBooth = 10;

            final int totalNumberOfAssignBooth = ThreadLocalRandom.current().nextInt(minimumBooth, maximumBooth + 1);
            final int totalNumberOfNeededEmployee = totalNumberOfAssignBooth * 4;

            //maximum needed employee will be 40, so get 45
            Page<CompanyEmployeeDTO> pageResult = companyEmployeeService.getAllCompanyEmployees(userDetails.getCompanyId(), "", 45, 0, "account.createTime", Sort.Direction.ASC);

            List<CompanyEmployeeDTO> listShuffle = new ArrayList<>();
            for (CompanyEmployeeDTO c : pageResult.getContent()) {
                listShuffle.add(c);
            }
            for (int j = 0; j <= totalNumberOfAssignBooth; j++) {
                Collections.shuffle(listShuffle);
                System.out.println(listShuffle);
                List<CompanyEmployeeDTO> neededEmployeeList = listShuffle
                        .stream()
                        .filter(item -> item.getAccount().getRole() != Role.COMPANY_MANAGER)
                        .limit(totalNumberOfNeededEmployee).collect(Collectors.toList());
                System.out.println(neededEmployeeList);

                //make sure these employee must be VERIFIED
                neededEmployeeList.forEach(item -> {
                    AccountDTO dto = item.getAccount();
                    dto.setStatus(AccountStatus.VERIFIED);
                    item.setAccount(dto);
                });
                //get first 4 employees from total needed employee
                //Employee 1: SUPERVISOR
                AssignmentDTO supervisorAssigment = assignmentService
                        .assignEmployee(
                                userDetails.getId(),
                                neededEmployeeList.get(0).getAccountId(),
                                jobFairBooths.get(j).getId(),
                                AssignmentType.SUPERVISOR,
                                userDetails.getCompanyId(),
                                1658730607000L,
                                1661409007000L);
                AssignmentDTO decoratorAssignment = assignmentService
                        .assignEmployee(
                                userDetails.getId(),
                                neededEmployeeList.get(1).getAccountId(),
                                jobFairBooths.get(j).getId(),
                                AssignmentType.DECORATOR,
                                userDetails.getCompanyId(),
                                1658730607000L,
                                1661409007000L
                        );
                AssignmentDTO staffAssignment_1 = assignmentService
                        .assignEmployee(
                                userDetails.getId(),
                                neededEmployeeList.get(2).getAccountId(),
                                jobFairBooths.get(j).getId(),
                                AssignmentType.STAFF,
                                userDetails.getCompanyId(),
                                1658730607000L,
                                1661409007000L
                        );
                AssignmentDTO staffAssignment_2 = assignmentService
                        .assignEmployee(
                                userDetails.getId(),
                                neededEmployeeList.get(3).getAccountId(),
                                jobFairBooths.get(j).getId(),
                                AssignmentType.STAFF,
                                userDetails.getCompanyId(),
                                1658730607000L,
                                1661409007000L
                        );
            }

            result.add(jobFairDTO);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping(ApiEndPoint.Demo.CREATE_1_DRAFT_JOB_FAIR_FOR_COMPANY)
    public ResponseEntity<?> create1DraftForEachCompany(@RequestBody List<String> companyIds) {
        for (String id : companyIds) {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            DraftJobFairRequest request = new DraftJobFairRequest();
            request.setName("CREATED_JOB_FAIR_DRAFT_" + companyIds.indexOf(id));
            request.setDecorateStartTime(1658730607000L);
            request.setDecorateEndTime(1661409007000L);
            request.setPublicStartTime(1661754607000L);
            request.setPublicEndTime(1667025007000L);
            request.setDescription("description about job fair");
            request.setTargetAttendant("Sinh vien FPT");
            request.setHostName("Demo haha");
            JobFairDTO jobFairDTO = jobFairMapper.toDTO(request);
            jobFairDTO.setThumbnailUrl("https://d3polnwtp0nqe6.cloudfront.net/JobFair-thumbnail/6b9f083b-9a95-4bd3-b264-4822ae76b8f6");


            jobFairDTO.setCompany(CompanyDTO.builder().id(id).build());
            jobFairDTO = jobFairService.createNewJobFair(jobFairDTO);
            layoutService.pickJobFairLayout(jobFairDTO.getId(), "b40ab83c-8f13-44ea-91b7-993f2263efae", id);


            /*
             * 1. Random total of booths that need assign
             * 2. Calculate total number of employee that need to be assigned: totalOfBooth * 4
             * 3. Use for i to assign  employee. In this loop, get first 4 employee from total of emmployee above, then assign equally to the each booth
             * */
            List<JobFairBoothDTO> jobFairBooths = jobFairBoothService.getCompanyBoothByJobFairId(jobFairDTO.getId());
            final Integer minimumBooth = 1;
            final Integer maximumBooth = 10;

            final int totalNumberOfAssignBooth = ThreadLocalRandom.current().nextInt(minimumBooth, maximumBooth + 1);
            final int totalNumberOfNeededEmployee = totalNumberOfAssignBooth * 4;

            //maximum needed employee will be 40, so get 45
            Page<CompanyEmployeeDTO> pageResult = companyEmployeeService.getAllCompanyEmployees(userDetails.getCompanyId(), "", 45, 0, "account.createTime", Sort.Direction.ASC);
            List<CompanyEmployeeDTO> neededEmployeeList = pageResult.getContent().stream().limit(totalNumberOfNeededEmployee).collect(Collectors.toList());

            for (int j = 0; j <= totalNumberOfAssignBooth; j++) {
                //get first 4 employees from total needed employee
                //Employee 1: SUPERVISOR
                AssignmentDTO supervisorAssigment = assignmentService
                        .assignEmployee(
                                userDetails.getId(),
                                neededEmployeeList.get(0).getAccountId(),
                                jobFairBooths.get(j).getId(),
                                AssignmentType.SUPERVISOR,
                                userDetails.getCompanyId(),
                                1658730607000L,
                                1661409007000L);
                AssignmentDTO decoratorAssignment = assignmentService
                        .assignEmployee(
                                userDetails.getId(),
                                neededEmployeeList.get(1).getAccountId(),
                                jobFairBooths.get(j).getId(),
                                AssignmentType.DECORATOR,
                                userDetails.getCompanyId(),
                                1658730607000L,
                                1661409007000L
                        );
                AssignmentDTO staffAssignment_1 = assignmentService
                        .assignEmployee(
                                userDetails.getId(),
                                neededEmployeeList.get(2).getAccountId(),
                                jobFairBooths.get(j).getId(),
                                AssignmentType.STAFF,
                                userDetails.getCompanyId(),
                                1658730607000L,
                                1661409007000L
                        );
                AssignmentDTO staffAssignment_2 = assignmentService
                        .assignEmployee(
                                userDetails.getId(),
                                neededEmployeeList.get(3).getAccountId(),
                                jobFairBooths.get(j).getId(),
                                AssignmentType.STAFF,
                                userDetails.getCompanyId(),
                                1658730607000L,
                                1661409007000L
                        );
            }
        }
        return ResponseEntity.ok("Created");
    }
}
