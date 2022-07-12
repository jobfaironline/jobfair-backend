package org.capstone.job_fair.controllers.demo;

import com.amazonaws.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.JobPositionConstant;
import org.capstone.job_fair.controllers.payload.requests.attendant.RegisterAttendantRequest;
import org.capstone.job_fair.controllers.payload.requests.attendant.cv.UpdateCvRequest;
import org.capstone.job_fair.controllers.payload.requests.company.BoothDescriptionRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.BoothJobPositionRequest;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.application.ApplicationDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.CvDTO;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.BoothJobPositionDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothDTO;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.services.interfaces.attendant.AttendantService;
import org.capstone.job_fair.services.interfaces.attendant.application.ApplicationService;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.interfaces.company.job.JobPositionService;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairService;
import org.capstone.job_fair.services.interfaces.job_fair.booth.JobFairBoothService;
import org.capstone.job_fair.services.interfaces.matching_point.MatchingPointService;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        Page<JobPositionDTO> jobPositions = jobPositionService.getAllJobPositionOfCompany(
                userDetails.getCompanyId(), null, null, null, 5, 0, "createdDate", Sort.Direction.ASC);

        //Get all job fair booths of job fair, then add job position to those booths
        List<JobFairBoothDTO> boothListDTO = jobFairBoothService.getCompanyBoothByJobFairId(jobFairId);

        for (JobFairBoothDTO dto : boothListDTO) {
            JobFairBoothDTO jobFairBoothDto = new JobFairBoothDTO();
            jobFairBoothDto.setId(dto.getId());
            jobFairBoothDto.setDescription("chay script");
            jobFairBoothDto.setName("chay script");
            //mapping job position to booth job position
            List<BoothJobPositionDTO> boothJobPositions = jobPositions.getContent().stream().map(item -> {
                BoothJobPositionDTO boothJobPosition = BoothJobPositionDTO
                        .builder()
                        .originJobPosition(item.getId())
                        .minSalary(Double.parseDouble("5"))
                        .maxSalary(Double.parseDouble("5"))
                        .numOfPosition(Integer.parseInt("1"))
                        .isHaveTest(false)
                        .note("abc")
                        .testTimeLength(Integer.parseInt("15"))
                        .numOfQuestion(Integer.parseInt("0"))
                        .passMark(Double.parseDouble("0"))
                        .jobFairBooth(jobFairBoothDto)
                        .descriptionKeyWord(item.getDescriptionKeyWord())
                        .requirementKeyWord(item.getRequirementKeyWord())
                        .build();
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

//    private void submitMultipleApplication(String jobFairId, String attendantId) {
//        //1. Draft multiple applications
//
//
//        List<BoothJobPositionDTO> boothJobPositions = createBoothJobPosition(jobFairId);
//
//
//        List<Map<String, String>> mapLists = createAttendantsWithOneCv(100);
//        for (Map<String, String> map : mapLists) {
//            ApplicationDTO dto = new ApplicationDTO();
//            AttendantDTO attendantDTO = new AttendantDTO();
//            AccountDTO accountDTO = new AccountDTO();
//            //set account Id
//            map.keySet().stream().forEach(userId -> {
//                accountDTO.setId(userId);
//                attendantDTO.setAccount(accountDTO);
//                dto.setAttendant(attendantDTO);
//
//                map.values().stream().forEach(cvId -> {
//                    for (BoothJobPositionDTO Dto : boothJobPositions) {
//                        dto.setCreateDate(new Date().getTime());
//                        dto.setStatus(ApplicationStatus.DRAFT);
//                        dto.setOriginCvId(cvId);
//                        dto.setBoothJobPositionDTO(Dto);
//                        //call create method
//                        ApplicationDTO result = applicationService.createNewApplication(dto);
//                        matchingPointService.calculateFromApplication(result.getId()).subscribe().dispose();
//                        //2. submit multiple application
//                        applicationService.submitApplication(result.getId(), userId);
//                    }
//                });
//            });
//
//        }
//
//    }

    private List<Map<String, String>> createAttendantsWithOneCv (Integer numberOfAttendants) {

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
    };

//    @GetMapping(ApiEndPoint.Demo.SUBMIT_MULTIPLE_APPLICATION)
//    public ResponseEntity<?> submitApplications(@RequestParam String jobFairId, @RequestParam String attendantId) {
//        submitMultipleApplication(jobFairId, attendantId);
//        return ResponseEntity.ok("submitted");
//    }

}
