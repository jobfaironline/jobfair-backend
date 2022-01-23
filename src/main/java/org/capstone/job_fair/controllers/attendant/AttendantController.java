package org.capstone.job_fair.controllers.attendant;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.RegisterAttendantRequest;
import org.capstone.job_fair.controllers.payload.requests.UpdateAttendantRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.attendant.AttendantService;
import org.capstone.job_fair.services.interfaces.attendant.CountryService;
import org.capstone.job_fair.services.interfaces.attendant.ResidenceService;
import org.capstone.job_fair.services.mappers.*;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class AttendantController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AttendantService attendantService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ResidenceService residenceService;

    @Autowired
    private SkillMapper skillMapper;

    @Autowired
    private WorkHistoryMapper workHistoryMapper;

    @Autowired
    private EducationMapper educationEntityMapper;

    @Autowired
    private CertificationMapper certMapper;

    @Autowired
    private ReferenceMapper refMapper;

    @Autowired
    private ActivityMapper actMapper;


    @GetMapping(ApiEndPoint.Attendant.ATTENDANT_ENDPOINT)
    public ResponseEntity<List<AttendantDTO>> getAllAccounts() {
        return new ResponseEntity<>(attendantService.getAllAttendants(), HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @PutMapping(ApiEndPoint.Attendant.UPDATE_ENDPOINT)
    public ResponseEntity<?> update(@Validated @RequestBody UpdateAttendantRequest request) {

        Optional<AccountEntity> opt = accountService.getActiveAccountById(request.getAccountId());
        if (!opt.isPresent()) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }

        if (opt.isPresent() && !opt.get().getEmail().equals(request.getAccount().getEmail()) && isEmailExist(request.getAccount().getEmail())) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Account.EMAIL_EXISTED),
                    HttpStatus.BAD_REQUEST);
        }

        if (!isCountryExist(request.getCountryID())) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND_COUNTRY),
                    HttpStatus.BAD_REQUEST);
        }

        if (!isResidenceExist(request.getResidenceID())) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND_RESIDENCE),
                    HttpStatus.BAD_REQUEST);
        }
        List<SkillDTO> skillDTOs = null;

        if (request.getSkillRequests() != null) {
            //if any field of request is null -> return error message
            if (request.getSkillRequests().stream().anyMatch(req -> !isNotNullSkillRequest(req))) {
                return GenericResponse.build(
                        MessageUtil.getMessage(MessageConstant.Skill.INVALID_SKILL),
                        HttpStatus.BAD_REQUEST);
            }

            skillDTOs = request.getSkillRequests()
                    .stream()
                    .filter(req -> isNotNullSkillRequest(req))
                    .map(req -> {
                        SkillDTO dto = new SkillDTO();
                        dto = skillMapper.toDTO(req);
                        //if want to update skill, send req that include skill's id
                        if (req.getId() != null) {
                            dto.setId(req.getId());
                        }
                        return dto;
                    }).collect(Collectors.toList());
        }


        List<WorkHistoryDTO> historyDTOs = null;
        if (request.getWorkHistoryRequests() != null) {
            if (request.getWorkHistoryRequests().stream().anyMatch(req -> !isNotNullWorkHistoryRequest(req))) {
                return GenericResponse.build(
                        MessageUtil.getMessage(MessageConstant.WorkHistory.INVALID_WORK_HISTORY),
                        HttpStatus.BAD_REQUEST);
            }

            historyDTOs = request.getWorkHistoryRequests()
                    .stream()
                    .filter(req -> isNotNullWorkHistoryRequest(req))
                    .map(req -> {
                        WorkHistoryDTO dto = new WorkHistoryDTO();
                        dto = workHistoryMapper.toDTO(req);
                        if (req.getId() != null) {
                            dto.setId(req.getId());
                        }
                        return dto;
                    }).collect(Collectors.toList());
        }

        List<EducationDTO> educationDTOs = null;
        if (request.getEducationRequests() != null) {
            if (request.getEducationRequests().stream().anyMatch(req -> !isNotNullEducation(req))) {
                return GenericResponse.build(
                        MessageUtil.getMessage(MessageConstant.Education.INVALID_EDUCATION),
                        HttpStatus.BAD_REQUEST);
            }

            educationDTOs = request.getEducationRequests()
                    .stream()
                    .filter(req -> isNotNullEducation(req))
                    .map(req -> {
                        EducationDTO dto = new EducationDTO();
                        dto = educationEntityMapper.toDTO(req);
                        if (req.getId() != null) {
                            dto.setId(req.getId());
                        }
                        return dto;
                    }).collect(Collectors.toList());
        }

        List<CertificationDTO> certificationDTOs = null;
        if (request.getCertificateRequests() != null) {
            if (request.getCertificateRequests().stream().anyMatch(req -> !isNotNullCertificate(req))) {
                return GenericResponse.build(
                        MessageUtil.getMessage(MessageConstant.Certification.INVALID_CERTIFICATION),
                        HttpStatus.BAD_REQUEST);
            }

            certificationDTOs = request.getCertificateRequests()
                    .stream()
                    .filter(req -> isNotNullCertificate(req))
                    .map(req -> {
                        CertificationDTO dto = new CertificationDTO();
                        dto = certMapper.toDTO(req);
                        if (req.getId() != null) {
                            dto.setId(req.getId());
                        }
                        return dto;
                    }).collect(Collectors.toList());
        }

        List<ReferenceDTO> referenceDTOs = null;
        if (request.getReferenceRequests() != null) {
            if (request.getReferenceRequests().stream().anyMatch(req -> !isNotNullReferenceRequest(req))) {
                return GenericResponse.build(
                        MessageUtil.getMessage(MessageConstant.Reference.INVALID_REFERENCE),
                        HttpStatus.BAD_REQUEST);
            }
            referenceDTOs = request.getReferenceRequests()
                    .stream()
                    .filter(req -> isNotNullReferenceRequest(req))
                    .map(req -> {
                        ReferenceDTO dto = new ReferenceDTO();
                        dto = refMapper.toDTO(req);
                        if (req.getId() != null) {
                            dto.setId(req.getId());
                        }
                        return dto;
                    }).collect(Collectors.toList());
        }

        List<ActivityDTO> activityDTOs = null;
        if (request.getActivityRequestList() != null) {
            if (request.getActivityRequestList().stream().anyMatch(req -> !isNotNullActivityRequest(req))) {
                return GenericResponse.build(
                        MessageUtil.getMessage(MessageConstant.Activity.INVALID_ACTIVITY),
                        HttpStatus.BAD_REQUEST);
            }
            activityDTOs = request.getActivityRequestList()
                    .stream()
                    .filter(req -> isNotNullActivityRequest(req))
                    .map(req -> {
                        ActivityDTO dto = new ActivityDTO();
                        dto = actMapper.toDTO(req);
                        if (req.getId() != null) {
                            dto.setId(req.getId());
                        }
                        return dto;
                    }).collect(Collectors.toList());
        }


        if (!isWorkHistoryValid(historyDTOs)) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Account.WORK_HISTORY_INVALID),
                    HttpStatus.BAD_REQUEST);
        }

        if (!isEducationValid(educationDTOs)) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Account.EDUCATION_INVALID),
                    HttpStatus.BAD_REQUEST);
        }

        if (!isActivityValid(activityDTOs)) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Account.ACTIVITY_INVALID),
                    HttpStatus.BAD_REQUEST);
        }

        AccountDTO accountDTO = AccountDTO.builder()
                .id(request.getAccountId())
                .email(request.getAccount().getEmail())
                .password(request.getPassword())
                .firstname(request.getAccount().getFirstname())
                .lastname(request.getAccount().getLastname())
                .middlename(request.getAccount().getMiddlename())
                .gender(request.getAccount().getGender())
                .role(Role.ATTENDANT)
                .phone(request.getAccount().getPhone())
                .profileImageUrl(request.getAccount().getProfileImageUrl())
                .status(AccountStatus.ACTIVE)
                .build();


        AttendantDTO attendantDTO = AttendantDTO.builder()
                .account(accountDTO)
                .title(request.getTitle())
                .address(request.getAddress())
                .dob(request.getDob())
                .jobTitle(request.getJobTitle())
                .yearOfExp(request.getYearOfExp())
                .maritalStatus(request.getMaritalStatus())
                .countryId(request.getCountryID())
                .residenceId(request.getResidenceID())
                .jobLevel(request.getJobLevel())
                .skills(skillDTOs)
                .workHistories(historyDTOs)
                .educations(educationDTOs)
                .certifications(certificationDTOs)
                .references(referenceDTOs)
                .activities(activityDTOs)
                .build();
        try {
            attendantService.updateAccount(attendantDTO);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return GenericResponse.build(
                MessageUtil.getMessage(MessageConstant.Attendant.UPDATE_PROFILE_SUCCESSFULLY),
                HttpStatus.OK);
    }

    @PostMapping(ApiEndPoint.Attendant.REGISTER_ENDPOINT)
    public ResponseEntity<?> register(@Validated @RequestBody RegisterAttendantRequest req) {

        if (!req.getPassword().equals(req.getConfirmPassword())) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.AccessControlMessage.CONFIRM_PASSWORD_MISMATCH),
                    HttpStatus.BAD_REQUEST);
        }

        if (isEmailExist(req.getAccount().getEmail())) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Account.EMAIL_EXISTED),
                    HttpStatus.BAD_REQUEST);
        }


        AccountDTO accountDTO = req.getAccount() != null ? AccountDTO.builder()
                .id(UUID.randomUUID().toString())
                .status(AccountStatus.ACTIVE)
                .lastname(req.getAccount().getLastname())
                .firstname(req.getAccount().getFirstname())
                .middlename(req.getAccount().getMiddlename())
                .email(req.getAccount().getEmail())
                .password(req.getPassword())
                .gender(req.getAccount().getGender())
                .role(Role.ATTENDANT)
                .phone(req.getAccount().getPhone())
                .build() : new AccountDTO();
        AttendantDTO dto = AttendantDTO.builder()
                .account(accountDTO)
                .build();


        attendantService.createNewAccount(dto);
        return GenericResponse.build(
                MessageUtil.getMessage(MessageConstant.Attendant.REGISTER_SUCCESSFULLY),
                HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.Attendant.ATTENDANT_ENDPOINT + "/{email}")
    public ResponseEntity<?> getAttendant(@PathVariable("email") String email) {
        return ResponseEntity.status(HttpStatus.OK).body(attendantService.getAttendantByEmail(email));
    }

    private boolean isEmailExist(String email) {
        return accountService.getCountAccountByEmail(email) != 0;
    }

    private boolean isCountryExist(String id) {
        return countryService.getCountCountryById(id) != 0;
    }

    private boolean isResidenceExist(String id) {
        return residenceService.getCountResidenceById(id) != 0;
    }

    private boolean isWorkHistoryValid(List<WorkHistoryDTO> workHistories) {
        for (WorkHistoryDTO dto : workHistories) {
            if (dto.getFromDate() > dto.getToDate()) {
                return false;
            }
        }
        return true;
    }

    private boolean isEducationValid(List<EducationDTO> educations) {
        for (EducationDTO dto : educations) {
            if (dto.getFromDate() > dto.getToDate()) {
                return false;
            }
        }
        return true;
    }


    private boolean isActivityValid(List<ActivityDTO> activities) {
        for (ActivityDTO dto : activities) {
            if (dto.getFromDate() > dto.getToDate()) {
                return false;
            }
        }
        return true;
    }

    private boolean isNotNullSkillRequest(UpdateAttendantRequest.SkillRequest request) {
        return request.getName() != null && request.getProficiency() != null;
    }

    private boolean isNotNullWorkHistoryRequest(UpdateAttendantRequest.WorkHistoryRequest request) {
        return request.getCompany() != null
                && request.getPosition() != null
                && request.getDescription() != null;
    }

    private boolean isNotNullEducation(UpdateAttendantRequest.EducationRequest request) {
        return request.getSubject() != null
                && request.getSchool() != null
                && request.getAchievement() != null;
    }

    private boolean isNotNullCertificate(UpdateAttendantRequest.CertificateRequest request) {
        return request.getName() != null
                && request.getInstitution() != null
                && request.getCertificationLink() != null;
    }

    private boolean isNotNullReferenceRequest(UpdateAttendantRequest.ReferenceRequest request) {
        return request.getCompany() != null
                && request.getPosition() != null
                && request.getFullname() != null;
    }

    private boolean isNotNullActivityRequest(UpdateAttendantRequest.ActivityRequest request) {
        return request.getDescription() != null;
    }
}
