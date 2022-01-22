package org.capstone.job_fair.services.impl.attendant;

import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
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
    private SkillEntityMapper skillMapper;

    @Autowired
    private WorkHistoryEntityMapper workHistoryEntityMapper;

    @Autowired
    private EducationEntityMapper educationEntityMapper;

    @Autowired
    private CertificationEntityMapper certificationEntityMapper;

    @Autowired
    private ReferenceEntityMapper referenceEntityMapper;

    @Autowired
    private ActivityEntityMapper activityEntityMapper;

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


        if (dto.getSkills().stream().count() != 0) {
            List<SkillEntity> skillEntities = dto.getSkills()
                    .stream().map(skillDTO -> {
                        SkillEntity skillEntity = skillMapper.toEntity(skillDTO);
                        skillEntity.setId(UUID.randomUUID().toString());
                        skillEntity.setAttendant(entity);
                        return skillRepository.save(skillEntity);
                    })
                    .collect(Collectors.toList());
        }

        if (dto.getWorkHistories().stream().count() != 0) {
            List<WorkHistoryEntity> workHistoryEntities = dto.getWorkHistories()
                    .stream().map(workHistoryDTO -> {
                        WorkHistoryEntity workHistoryEntity = workHistoryEntityMapper.toEntity(workHistoryDTO);
                        workHistoryEntity.setId(UUID.randomUUID().toString());
                        workHistoryEntity.setAttendant(entity);
                        return workHistoryRepository.save(workHistoryEntity);
                    })
                    .collect(Collectors.toList());
        }

        if (dto.getEducations().stream().count() != 0) {
            List<EducationEntity> educationEntities = dto.getEducations()
                    .stream().map(educationDTO -> {
                        EducationEntity educationEntity = educationEntityMapper.toEntity(educationDTO);
                        QualificationEntity qualificationEntity = qualificationRepository.findById(educationDTO.getQualificationId()).get();
                        educationEntity.setQualification(qualificationEntity);
                        educationEntity.setId(UUID.randomUUID().toString());
                        educationEntity.setAttendant(entity);
                        return educationRepository.save(educationEntity);
                    })
                    .collect(Collectors.toList());
        }

        if (dto.getCertifications().stream().count() != 0) {
            List<CertificationEntity> certificationEntities = dto.getCertifications()
                    .stream().map(certificationDTO -> {
                        CertificationEntity certificationEntity = certificationEntityMapper.toEntity(certificationDTO);
                        certificationEntity.setId(UUID.randomUUID().toString());
                        certificationEntity.setAttendant(entity);
                        return certificationRepository.save(certificationEntity);
                    })
                    .collect(Collectors.toList());
        }

        if (dto.getReferences().stream().count() != 0) {
            List<ReferenceEntity> referenceEntities = dto.getReferences()
                    .stream().map(referenceDTO -> {
                        ReferenceEntity referenceEntity = referenceEntityMapper.toEntity(referenceDTO);
                        referenceEntity.setId(UUID.randomUUID().toString());
                        referenceEntity.setAttendant(entity);
                        return referenceRepository.save(referenceEntity);
                    })
                    .collect(Collectors.toList());
        }

        if (dto.getActivities().stream().count() != 0) {
            List<ActivityEntity> activityEntities = dto.getActivities()
                    .stream().map(activityDTO -> {
                        ActivityEntity activityEntity = activityEntityMapper.toEntity(activityDTO);
                        activityEntity.setId(UUID.randomUUID().toString());
                        activityEntity.setAttendant(entity);
                        return activityRepository.save(activityEntity);
                    })
                    .collect(Collectors.toList());
        }

        attendantRepository.save(entity);
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

}
