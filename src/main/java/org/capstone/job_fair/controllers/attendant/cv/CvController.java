package org.capstone.job_fair.controllers.attendant.cv;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.CreateCvRequest;
import org.capstone.job_fair.controllers.payload.GenericMessageResponseEntity;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.CountryDTO;
import org.capstone.job_fair.models.dtos.attendant.JobLevelDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.CountryEntity;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.NationalityEntity;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.attendant.cv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class CvController {

    private static final String NATION_ID_NOT_FOUND = "Nationality not found with id: ";
    private static final String COUNTRY_ID_NOT_FOUND = "Country not found with id: ";
    private static final String JOB_LEVEL_NOT_FOUND = "Current job level not found with id: ";
    private static final String ACCOUNT_NOT_FOUND = "Account not found with id: ";
    private static final String CREATE_CV_SUCCESS = "Create CV successfully";
    private static final String EMAIL_NOT_FOUND = "Email not found";
    private static final String EMPTY_FIRST_NAME = "First name must not be empty";
    private static final String EMPTY_LAST_NAME = "Last name must not be empty";
    private static final String YEAR_OF_EXP_INVALID = "Year of exp must be greater than 0.";
    private static final String CREATE_DATE_INVALID = "Create date must lower or equal today.";
    private static final String SKILL_INVALID = "Skill is invalid";
    private static final String WORK_HISTORY_INVALID = "Work history is invalid";
    private static final String EDUCATION_INVALID = "Education is invalid";
    private static final String CERT_INVALID = "Certification is invalid";
    private static final String ACTIVITY_INVALID = "Activity is invalid";
    private static final String DOB_INVALID = "Date of birth is invalid";

    @Autowired
    private CvService cvService;

    @Autowired
    private NationalityService nationalityService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private JobLevelService jobLevelService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private QualificationService qualificationService;

    private Optional<NationalityEntity> getNationalityById(String id) {
        return nationalityService.getNationalityById(id);
    }

    private Optional<CountryEntity> getCountryById(String id) {
        return countryService.getCountryById(id);
    }

    private Optional<JobLevelEntity> getJobLevelById(int id) {
        return jobLevelService.getJobLevelById(id);
    }

    private Optional<AccountEntity> getAccountById(String id) {
        return accountService.getActiveAccountById(id);
    }

    private Optional<AccountEntity> getAccountByEmail(String email) {
        return accountService.getActiveAccountByEmail(email);
    }

    private AccountDTO buildAccountDTO(CreateCvRequest req) {
        Optional<AccountEntity> opt = getAccountById(req.getAccountId());

        Optional<AccountEntity> optByEmail = getAccountByEmail(req.getEmail());

        if (!opt.isPresent() || !optByEmail.isPresent()) {
            return null;
        }

        AccountDTO dto = AccountDTO.builder()
                .id(req.getAccountId())
                .lastname(req.getAccount().getLastname())
                .firstname(req.getAccount().getFirstname())
                .middlename(req.getAccount().getMiddlename())
                .email(req.getAccount().getEmail())
                .gender(req.getAccount().getGender())
                .phone(req.getAccount().getPhone())
                .profileImageUrl(req.getAccount().getProfileImageUrl())
                .build();
        return dto;
    }

    private JobLevelDTO buildJobLevelDTO(CreateCvRequest req) {
        Optional<JobLevelEntity> optJob = getJobLevelById(req.getJobLevelId());
        if (!optJob.isPresent()) {
            return null;
        }
        JobLevelDTO dto = JobLevelDTO.builder()
                .id(req.getJobLevelId())
                .name(optJob.get().getName())
                .build();
        return dto;
    }

    private CountryDTO buildCountryDTO(CreateCvRequest req) {
        Optional<CountryEntity> optCountry = getCountryById(req.getCountryId());
        if (!optCountry.isPresent()) {
            return null;
        }
        CountryDTO dto = CountryDTO.builder()
                .id(req.getCountryId())
                .name(optCountry.get().getName())
                .description(optCountry.get().getDescription())
                .build();
        return dto;
    }

    private NationalityDTO buildNationalityDTO(CreateCvRequest req) {
        Optional<NationalityEntity> optNation = getNationalityById(req.getNationalityId());
        if (!optNation.isPresent()) {
            return null;
        }
        NationalityDTO dto = NationalityDTO.builder()
                .id(req.getNationalityId())
                .name(optNation.get().getName())
                .description(optNation.get().getDescription())
                .build();
        return dto;
    }


    @PostMapping(ApiEndPoint.Cv.CREATE_CV)
    public ResponseEntity<?> createCv(@Valid @RequestBody CreateCvRequest req) {
        NationalityDTO nationDTO = buildNationalityDTO(req);
        if (nationDTO == null) {
            return GenericMessageResponseEntity.build(NATION_ID_NOT_FOUND + req.getNationalityId(), HttpStatus.BAD_REQUEST);
        }

        CountryDTO countryDTO = buildCountryDTO(req);
        if (countryDTO == null) {
            return GenericMessageResponseEntity.build(COUNTRY_ID_NOT_FOUND + req.getCountryId(), HttpStatus.BAD_REQUEST);
        }

        JobLevelDTO jobLevelDTO = buildJobLevelDTO(req);
        if (jobLevelDTO == null) {
            return GenericMessageResponseEntity.build(JOB_LEVEL_NOT_FOUND + req.getJobLevelId(), HttpStatus.BAD_REQUEST);
        }

        AccountDTO accountDTO = buildAccountDTO(req);
        if (accountDTO == null) {
            return GenericMessageResponseEntity.build(ACCOUNT_NOT_FOUND + req.getAccountId(), HttpStatus.BAD_REQUEST);
        }

        if (req.getFirstname().isEmpty()) {
            return GenericMessageResponseEntity.build(EMPTY_FIRST_NAME, HttpStatus.BAD_REQUEST);
        }
        if (req.getLastname().isEmpty()) {
            return GenericMessageResponseEntity.build(EMPTY_LAST_NAME, HttpStatus.BAD_REQUEST);
        }

        if (req.getDob() >= new Date().getTime()) {
            return GenericMessageResponseEntity.build(DOB_INVALID, HttpStatus.BAD_REQUEST);
        }

        if (accountService.getCountByActiveEmail(req.getEmail()) == 0) {
            return GenericMessageResponseEntity.build(EMAIL_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        if (req.getYearOfExp() < 0) {
            return GenericMessageResponseEntity.build(YEAR_OF_EXP_INVALID, HttpStatus.BAD_REQUEST);
        }

        if (req.getCreateDate() > new Date().getTime()) {
            return GenericMessageResponseEntity.build(CREATE_DATE_INVALID, HttpStatus.BAD_REQUEST);
        }

        if (!isValidSkillDTO(req.getSkills())) {
            return GenericMessageResponseEntity.build(SKILL_INVALID, HttpStatus.BAD_REQUEST);
        }

        if (!isValidWorkHistory(req.getHistories())) {
            return GenericMessageResponseEntity.build(WORK_HISTORY_INVALID, HttpStatus.BAD_REQUEST);
        }

        if (!isValidEducation(req.getEducations())) {
            return GenericMessageResponseEntity.build(EDUCATION_INVALID, HttpStatus.BAD_REQUEST);
        }

        if (!isValidCert(req.getCertifications())) {
            return GenericMessageResponseEntity.build(CERT_INVALID, HttpStatus.BAD_REQUEST);
        }

        if (!isValidActivity(req.getActivities())) {
            return GenericMessageResponseEntity.build(ACTIVITY_INVALID, HttpStatus.BAD_REQUEST);
        }

        CvDTO cvDTO = CvDTO.builder()
                .firstname(req.getFirstname().trim())
                .lastname(req.getLastname().trim())
                .middlename(req.getMiddlename().trim())
                .yearOfExp(req.getYearOfExp())
                .email(req.getEmail().trim())
                .phone(req.getPhone().trim())
                .dob(req.getDob())
                .maritalStatus(req.getMaritalStatus())
                .cvStatus(req.getStatus())
                .address(req.getAddress().trim())
                .summary(req.getSummary())
                .createDate(new Date().getTime())
                .nationality(nationDTO)
                .country(countryDTO)
                .currentJob(jobLevelDTO)
                .gender(req.getGender())
                .account(accountDTO)
                // one to many
                .skills(req.getSkills())
                .workHistories(req.getHistories())
                .educations(req.getEducations())
                .certificates(req.getCertifications())
                .references(req.getReferences())
                .activities(req.getActivities())
                .build();


        cvService.createNewCv(cvDTO);
        return GenericMessageResponseEntity.build(CREATE_CV_SUCCESS, HttpStatus.OK);
    }

    private boolean isValidSkillDTO(List<SkillDTO> skills) {
        for (SkillDTO dto : skills) {
            if (dto.getProficiency() < 1) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidWorkHistory(List<WorkHistoryDTO> histories) {
        for(WorkHistoryDTO dto : histories){
            if (dto.getFromDate() >= dto.getToDate()) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidEducation(List<EducationDTO> educations) {
        for (EducationDTO dto : educations) {
            String qualificationId = dto.getQualificationId();
            int existedQual = qualificationService.getCountQualificationById(qualificationId);
            if (dto.getSubject().isEmpty() || dto.getFromDate() >= dto.getToDate() || existedQual == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidCert(List<CertificationDTO> certificates) {
        for (CertificationDTO dto : certificates) {
            if (dto.getName().isEmpty() || dto.getYear() > Calendar.getInstance().get(Calendar.YEAR)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidActivity(List<ActivityDTO> activities) {
        for (ActivityDTO dto : activities) {
            if (dto.getName().isEmpty() || dto.getFunctionTitle().isEmpty()
            || dto.getOrganization().isEmpty() || dto.getFromDate() >= dto.getToDate()) {
                return false;
            }
        }
        return true;
    }

}
