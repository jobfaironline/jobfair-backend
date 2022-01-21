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
import org.capstone.job_fair.services.interfaces.attendant.*;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    private JobLevelService jobLevelService;

    @Autowired
    private QualificationService qualicationService;

    @GetMapping(ApiEndPoint.Attendant.ATTENDANT_ENDPOINT)
    public ResponseEntity<List<AttendantDTO>> getAllAccounts() {
        return new ResponseEntity<>(attendantService.getAllAttendants(), HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @PutMapping(ApiEndPoint.Attendant.UPDATE_ENDPOINT)
    public ResponseEntity<?> update(@Validated @RequestBody UpdateAttendantRequest request) {

        Optional<AccountEntity> opt = accountService.getActiveAccountById(request.getAccountId());
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

        List<SkillDTO> skillDTOs = request.getSkillRequests()
                .stream().map(req -> {
                    SkillDTO dto = SkillDTO.builder()
                            .id(UUID.randomUUID().toString())
                            .name(req.getName())
                            .proficiency(req.getProficiency())
                            .build();
                    return dto;
                }).collect(Collectors.toList());

        List<WorkHistoryDTO> historyDTOs = request.getWorkHistoryRequests()
                .stream().map(req -> {
                    WorkHistoryDTO dto = WorkHistoryDTO.builder()
                            .id(UUID.randomUUID().toString())
                            .position(req.getPosition())
                            .company(req.getCompany())
                            .fromDate(req.getFromDate())
                            .toDate(req.getToDate())
                            .isCurrentJob(req.getIsCurrentJob())
                            .description(req.getDescription())
                            .build();
                    return dto;
                }).collect(Collectors.toList());

        List<EducationDTO> educationDTOs = request.getEducationRequests()
                .stream().map(req -> {
                    EducationDTO dto = EducationDTO.builder()
                            .id(UUID.randomUUID().toString())
                            .subject(req.getSubject())
                            .school(req.getSchool())
                            .fromDate(req.getFromDate())
                            .toDate(req.getToDate())
                            .achievement(req.getAchievement())
                            .qualificationId(req.getQualification().ordinal())
                            .build();
                    return dto;
                }).collect(Collectors.toList());

        List<CertificationDTO> certificationDTOs = request.getCertificateRequests()
                .stream().map(req -> {
                    CertificationDTO dto = CertificationDTO.builder()
                            .id(UUID.randomUUID().toString())
                            .name(req.getName())
                            .institution(req.getInstitution())
                            .year(req.getYear())
                            .certificationLink(req.getCertificationLink())
                            .build();
                    return dto;
                }).collect(Collectors.toList());

        List<ReferenceDTO> referenceDTOs = request.getReferenceRequests()
                .stream().map(req -> {
                    ReferenceDTO dto = ReferenceDTO.builder()
                            .id(UUID.randomUUID().toString())
                            .fullname(req.getFullname())
                            .position(req.getPosition())
                            .company(req.getCompany())
                            .email(req.getEmail())
                            .phone(req.getPhone())
                            .build();
                    return dto;
                }).collect(Collectors.toList());

        List<ActivityDTO> activityDTOs = request.getActivityRequestList()
                .stream().map(req -> {
                    ActivityDTO dto = ActivityDTO.builder()
                            .id(UUID.randomUUID().toString())
                            .name(req.getName())
                            .functionTitle(req.getFunctionTitle())
                            .organization(req.getOrganization())
                            .fromDate(req.getFromDate())
                            .toDate(req.getToDate())
                            .isCurrentActivity(req.getIsCurrentActivity())
                            .description(req.getDescription())
                            .build();
                    return dto;
                }).collect(Collectors.toList());


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
                .status(request.getAccount().getStatus())
                .firstname(request.getAccount().getFirstname())
                .lastname(request.getAccount().getLastname())
                .middlename(request.getAccount().getMiddlename())
                .gender(request.getAccount().getGender())
                .role(Role.ATTENDANT)
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
        attendantService.updateAccount(attendantDTO);
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

        if (isEmailExist(req.getAccount().getEmail())){
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
                .profileImageUrl(req.getAccount().getProfileImageUrl())
                .build() : new AccountDTO();
        AttendantDTO dto = AttendantDTO.builder()
                .account(accountDTO)
                .jobTitle(req.getJobTitle())
                .address(req.getAddress())
                .dob(req.getDob())
                .yearOfExp(req.getYearOfExp())
                .title(req.getTitle())
                .maritalStatus(req.getMaritalStatus())
                .countryId(req.getCountry())
                .residenceId(req.getResidence())
                .jobLevel(req.getCurrentJobLevel())
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
        return accountService.getCountByActiveEmail(email) != 0;
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
}
