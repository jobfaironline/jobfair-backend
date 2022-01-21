package org.capstone.job_fair.services.impl.attendant;

import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.account.GenderEntity;
import org.capstone.job_fair.models.entities.account.RoleEntity;
import org.capstone.job_fair.models.entities.attendant.*;
import org.capstone.job_fair.models.entities.attendant.cv.*;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.repositories.attendant.cv.*;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.attendant.QualificationService;
import org.capstone.job_fair.services.mappers.*;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.repositories.attendant.AttendantRepository;
import org.capstone.job_fair.services.interfaces.attendant.AttendantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    public void createNewAccount(AttendantDTO dto) {
        if (dto.getAccount().getProfileImageUrl() == null){
            dto.getAccount().setProfileImageUrl(AccountConstant.DEFAULT_PROFILE_IMAGE_URL);
        }

        AttendantEntity entity = mapper.toEntity(dto);
        String hashPassword = BCrypt.hashpw(dto.getAccount().getPassword(), BCrypt.gensalt(12));
        AccountEntity accountEntity = entity.getAccount();
        String accountId = UUID.randomUUID().toString();
        accountEntity.setId(accountId);

        entity.getAccount().setPassword(hashPassword);
        entity.setAccountId(accountId);


        accountRepository.save(accountEntity);

        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setId(dto.getCountryId());

        ResidenceEntity residenceEntity = new ResidenceEntity();
        residenceEntity.setId(dto.getResidenceId());

        JobLevelEntity jobLevelEntity = new JobLevelEntity();
        jobLevelEntity.setId(dto.getJobLevelId());

        entity.setCountry(countryEntity);
        entity.setResidence(residenceEntity);
        entity.setCurrentJobLevel(jobLevelEntity);


        List<SkillEntity> skillEntities = dto.getSkills()
                .stream().map(skillDTO -> {
                    SkillEntity skillEntity = skillMapper.toEntity(skillDTO);
                    return skillRepository.save(skillEntity);
                })
                .collect(Collectors.toList());

        List<WorkHistoryEntity> workHistoryEntities = dto.getWorkHistories()
                .stream().map(workHistoryDTO -> {
                    WorkHistoryEntity workHistoryEntity = workHistoryEntityMapper.toEntity(workHistoryDTO);
                    return workHistoryRepository.save(workHistoryEntity);
                })
                .collect(Collectors.toList());

        List<EducationEntity> educationEntities = dto.getEducations()
                .stream().map(educationDTO -> {
                    EducationEntity educationEntity =  educationEntityMapper.toEntity(educationDTO);
                    QualificationEntity qualificationEntity = qualificationRepository.findById(educationDTO.getQualificationId()).get();
                    educationEntity.setQualification(qualificationEntity);
                    return educationRepository.save(educationEntity);
                })
                .collect(Collectors.toList());

        List<CertificationEntity> certificationEntities = dto.getCertifications()
                .stream().map(certificationDTO -> {
                    CertificationEntity certificationEntity = certificationEntityMapper.toEntity(certificationDTO);
                    return certificationRepository.save(certificationEntity);
                })
                .collect(Collectors.toList());

        List<ReferenceEntity> referenceEntities = dto.getReferences()
                .stream().map(referenceDTO -> {
                    ReferenceEntity referenceEntity = referenceEntityMapper.toEntity(referenceDTO);
                    return referenceRepository.save(referenceEntity);
                })
                .collect(Collectors.toList());

        List<ActivityEntity> activityEntities = dto.getActivities()
                .stream().map(activityDTO -> {
                    ActivityEntity activityEntity = activityEntityMapper.toEntity(activityDTO);
                    return activityRepository.save(activityEntity);
                })
                .collect(Collectors.toList());

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
    public AttendantEntity update(AttendantDTO attendantDTO) {
        return attendantRepository.findById(attendantDTO.getAccount().getId()).map((atd) -> {
            mapper.updateAttendantMapperFromDto(attendantDTO, atd);
            if (attendantDTO.getAccount().getGender() != null) {
                GenderEntity genderEntity = new GenderEntity();
                genderEntity.setId(attendantDTO.getAccount().getGender().ordinal());
                atd.getAccount().setGender(genderEntity);
            }
            return attendantRepository.save(atd);
        }).orElseThrow(() -> new NoSuchElementException("Account not found"));
    }

    @Override
    public List<AttendantDTO> getAllAttendants() {
        return attendantRepository.findAll().stream().map(entity -> mapper.toDTO(entity)).collect(Collectors.toList());
    }

}
