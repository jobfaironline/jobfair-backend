package org.capstone.job_fair.services.impl.attendant;

import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.*;
import org.capstone.job_fair.models.entities.attendant.cv.*;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.repositories.attendant.AttendantRepository;
import org.capstone.job_fair.repositories.attendant.cv.*;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.attendant.AttendantService;
import org.capstone.job_fair.services.mappers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
public class AttendantServiceImpl implements AttendantService {

    @Autowired
    private AttendantEntityMapper mapper;

    @Autowired
    private AccountEntityMapper accountMapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private AttendantRepository attendantRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SkillMapper skillMapper;

    @Autowired
    private WorkHistoryMapper workHistoryMapper;

    @Autowired
    private EducationMapper educationEntityMapper;

    @Autowired
    private CertificationMapper certificationEntityMapper;

    @Autowired
    private ReferenceMapper referenceEntityMapper;

    @Autowired
    private ActivityMapper activityEntityMapper;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private WorkHistoryRepository workHistoryRepository;

    @Autowired
    private EducationRepository educationRepository;


    @Autowired
    private CertificationRepository certificationRepository;

    @Autowired
    private ReferenceRepository referenceRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private QualificationRepository qualificationRepository;


    @Override
    public void updateAccount(AttendantDTO dto) {

        if (dto.getAccount().getProfileImageUrl() == null) {
            dto.getAccount().setProfileImageUrl(AccountConstant.DEFAULT_PROFILE_IMAGE_URL);
        }

        AttendantEntity entity = mapper.toEntity(dto);
        AccountEntity accountEntity = entity.getAccount();
        accountEntity.setId(dto.getAccount().getId());

        entity.setAccountId(dto.getAccount().getId());

        accountRepository.save(accountEntity);

        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setId(dto.getCountryId());

        ResidenceEntity residenceEntity = new ResidenceEntity();
        residenceEntity.setId(dto.getResidenceId());

        JobLevelEntity jobLevelEntity = new JobLevelEntity();
        jobLevelEntity.setId(dto.getJobLevel().ordinal());

        entity.setCountry(countryEntity);
        entity.setResidence(residenceEntity);
        entity.setCurrentJobLevel(jobLevelEntity);
        attendantRepository.save(entity);


        if (dto.getSkills() != null) {
            dto.getSkills().forEach(skillDTO -> {
                if (skillDTO.getId() == null) { // if skill id null: create new skill
                    createNewSkill(skillDTO, entity);
                } else {
                    updateSkill(skillDTO, entity); // else update skill
                }
            });
        }

        if (dto.getWorkHistories() != null) {
            dto.getWorkHistories().forEach(workHistoryDTO -> {
                if (workHistoryDTO.getId() == null) {
                    createNewWorkHistory(workHistoryDTO, entity);
                } else {
                    updateWorkHistory(workHistoryDTO, entity);
                }
            });
        }

        if (dto.getEducations() != null) {
            dto.getEducations().forEach(educationDTO -> {
                if (educationDTO.getId() == null) {
                    createEducation(educationDTO, entity);
                } else {
                    updateEducation(educationDTO, entity);
                }
            });
        }

        if (dto.getCertifications() != null) {
            dto.getCertifications().forEach(certificationDTO -> {
                if (certificationDTO.getId() == null) {
                    createCertification(certificationDTO, entity);
                } else {
                    updateCertification(certificationDTO, entity);
                }
            });
        }

        if (dto.getReferences() != null) {
            dto.getReferences().forEach(referenceDTO -> {
                if (referenceDTO.getId() == null) {
                    createReference(referenceDTO, entity);
                } else {
                    updateReference(referenceDTO, entity);
                }
            });
        }

        if (dto.getActivities() != null) {
            dto.getActivities().forEach(activityDTO -> {
                if (activityDTO.getId() == null) {
                    createActivity(activityDTO, entity);
                }
                else {
                    updateActivity(activityDTO, entity);
                }
            });
        }

    }

    @Override
    public Optional<AttendantDTO> getAttendantByEmail(String email) {
        Optional<AccountEntity> accountOpt = accountService.getActiveAccountByEmail(email);
        if (!accountOpt.isPresent()) {
            return Optional.empty();
        }
        Optional<AttendantEntity> attendantOpt = attendantRepository.findById(accountOpt.get().getId());
        if (!attendantOpt.isPresent()) {
            return Optional.empty();
        }
        AttendantDTO dto = mapper.toDTO(attendantOpt.get());
        return Optional.of(dto);
    }

    @Override
    public AttendantEntity createNewAccount(AttendantDTO dto) {
        AttendantEntity attendantEntity = mapper.toEntity(dto);
        AccountEntity accountEntity = accountMapper.toEntity(dto.getAccount());
        attendantEntity.setAccountId(dto.getAccount().getId());
        String hashPassword = encoder.encode(dto.getAccount().getPassword());
        accountEntity.setPassword(hashPassword);
        accountEntity.setProfileImageUrl(AccountConstant.DEFAULT_PROFILE_IMAGE_URL);


        return attendantRepository.save(attendantEntity);

    }

    @Override
    public List<AttendantDTO> getAllAttendants() {
        return attendantRepository.findAll().stream().map(entity -> mapper.toDTO(entity)).collect(Collectors.toList());
    }

    //Create skill
    private void createNewSkill(SkillDTO dto, AttendantEntity entity) {
        SkillEntity skillEntity = skillMapper.toEntity(dto);
        skillEntity.setId(UUID.randomUUID().toString());
        skillEntity.setAttendant(entity);
        skillRepository.save(skillEntity);
    }

    //Update skill
    private void updateSkill(SkillDTO dto, AttendantEntity entity) {
        if (!skillRepository.findById(dto.getId()).isPresent()) {
            throw new NoSuchElementException(MessageConstant.Skill.SKILL_NOT_FOUND);
        }
        SkillEntity skillEntity = skillMapper.toEntity(dto);
        skillEntity.setId(dto.getId());
        skillEntity.setAttendant(entity);
        skillRepository.save(skillEntity);
    }
    //End create-update skill

    //Create work history
    private void createNewWorkHistory(WorkHistoryDTO dto, AttendantEntity entity) {
        WorkHistoryEntity workHistoryEntity = workHistoryMapper.toEntity(dto);
        workHistoryEntity.setId(UUID.randomUUID().toString());
        workHistoryEntity.setAttendant(entity);
        workHistoryRepository.save(workHistoryEntity);
    }

    //Update work history
    private void updateWorkHistory(WorkHistoryDTO dto, AttendantEntity entity) {
        if (!workHistoryRepository.findById(dto.getId()).isPresent()) {
            throw new NoSuchElementException(MessageConstant.WorkHistory.WORK_HISTORY_NOT_FOUND);
        }
        WorkHistoryEntity workHistoryEntity = workHistoryMapper.toEntity(dto);
        workHistoryEntity.setId(dto.getId());
        workHistoryEntity.setAttendant(entity);
        workHistoryRepository.save(workHistoryEntity);
    }
    //End create-update work history

    //Create Education
    private void createEducation(EducationDTO dto, AttendantEntity entity) {
        EducationEntity educationEntity = educationEntityMapper.toEntity(dto);
        QualificationEntity qualificationEntity = qualificationRepository.findById(dto.getQualificationId()).get();
        if (qualificationEntity == null) {
            throw new NoSuchElementException(MessageConstant.Qualification.INVALID_QUALIFICATION);
        }
        educationEntity.setQualification(qualificationEntity);
        educationEntity.setId(UUID.randomUUID().toString());
        educationEntity.setAttendant(entity);
        educationRepository.save(educationEntity);
    }

    //Update education
    private void updateEducation(EducationDTO dto, AttendantEntity entity) {
        if (!educationRepository.findById(dto.getId()).isPresent()) {
            throw new NoSuchElementException(MessageConstant.Education.EDUCATION_NOT_FOUND);
        }
        EducationEntity educationEntity = educationEntityMapper.toEntity(dto);
        QualificationEntity qualificationEntity = qualificationRepository.findById(dto.getQualificationId()).get();
        if (qualificationEntity == null) {
            throw new NoSuchElementException(MessageConstant.Qualification.INVALID_QUALIFICATION);
        }
        educationEntity.setQualification(qualificationEntity);
        educationEntity.setId(dto.getId());
        educationEntity.setAttendant(entity);
        educationRepository.save(educationEntity);
    }
    //End create-update education

    //Create certification
    private void createCertification(CertificationDTO dto, AttendantEntity entity) {
        CertificationEntity certificationEntity = certificationEntityMapper.toEntity(dto);
        certificationEntity.setId(UUID.randomUUID().toString());
        certificationEntity.setAttendant(entity);
        certificationRepository.save(certificationEntity);
    }

    //update certification
    private void updateCertification(CertificationDTO dto, AttendantEntity entity) {
        if (!certificationRepository.findById(dto.getId()).isPresent()) {
            throw new NoSuchElementException(MessageConstant.Certification.CERTIFICATION_NOT_FOUND);
        }
        CertificationEntity certificationEntity = certificationEntityMapper.toEntity(dto);
        certificationEntity.setId(dto.getId());
        certificationEntity.setAttendant(entity);
        certificationRepository.save(certificationEntity);
    }
    //End create-update certification

    //Create reference
    private void createReference(ReferenceDTO dto, AttendantEntity entity) {
        ReferenceEntity referenceEntity = referenceEntityMapper.toEntity(dto);
        referenceEntity.setId(UUID.randomUUID().toString());
        referenceEntity.setAttendant(entity);
        referenceRepository.save(referenceEntity);
    }

    //update reference
    private void updateReference(ReferenceDTO dto, AttendantEntity entity) {
        if (!referenceRepository.findById(dto.getId()).isPresent()) {
            throw new NoSuchElementException(MessageConstant.Reference.REFERENCE_NOT_FOUND);
        }
        ReferenceEntity referenceEntity = referenceEntityMapper.toEntity(dto);
        referenceEntity.setId(dto.getId());
        referenceEntity.setAttendant(entity);
        referenceRepository.save(referenceEntity);
    }
    //end create-update reference

    //create activity
    private void createActivity(ActivityDTO dto, AttendantEntity entity) {
        ActivityEntity activityEntity = activityEntityMapper.toEntity(dto);
        activityEntity.setId(UUID.randomUUID().toString());
        activityEntity.setAttendant(entity);
        activityRepository.save(activityEntity);
    }
    //update activity
    private void updateActivity(ActivityDTO dto, AttendantEntity entity) {
        if (!activityRepository.findById(dto.getId()).isPresent()) {
            throw new NoSuchElementException(MessageConstant.Activity.ACTIVITY_NOT_FOUND);
        }
        ActivityEntity activityEntity = activityEntityMapper.toEntity(dto);
        activityEntity.setId(dto.getId());
        activityEntity.setAttendant(entity);
        activityRepository.save(activityEntity);
    }

}
