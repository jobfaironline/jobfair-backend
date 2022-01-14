package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.account.GenderEntity;
import org.capstone.job_fair.models.entities.attendant.CountryEntity;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.models.entities.attendant.NationalityEntity;
import org.capstone.job_fair.models.entities.attendant.cv.*;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.repositories.attendant.CountryRepository;
import org.capstone.job_fair.repositories.attendant.JobLevelRepository;
import org.capstone.job_fair.repositories.attendant.NationalityRepository;
import org.capstone.job_fair.repositories.attendant.cv.*;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.mappers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CvServiceImpl implements CvService {

    @Autowired
    private CvRepository cvRepository;

    @Autowired
    private CvEntityMapper mapper;

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
    private ActivityRepository activityRep;

    @Autowired
    private SkillEntityMapper skillMapper;

    @Autowired
    private WorkHistoryEntityMapper workHistoryMapper;

    @Autowired
    private EducationEntityMapper educationMapper;

    @Autowired
    private CertificationEntityMapper certificationMapper;

    @Autowired
    private ReferenceEntityMapper referenceMapper;

    @Autowired
    private ActivityEntityMapper activityMapper;

    @Transactional
    @Override
    public void createNewCv(CvDTO dto) {
        CvEntity cvEntity = new CvEntity();
        mapper.DTOToEntity(dto, cvEntity);
        String id = UUID.randomUUID().toString();
        cvEntity.setId(id);


        NationalityEntity nationality = new NationalityEntity();
        nationality.setId(dto.getNationality().getId());
        cvEntity.setNationality(nationality);

        CountryEntity country = new CountryEntity();
        country.setId(dto.getCountry().getId());
        cvEntity.setCountry(country);

        JobLevelEntity jobLevel = new JobLevelEntity();
        jobLevel.setId(dto.getCurrentJob().getId());
        cvEntity.setCurrentJobLevel(jobLevel);

        GenderEntity gender = new GenderEntity();
        gender.setId(dto.getAccount().getGender().ordinal());
        cvEntity.setGender(gender);

        AccountEntity account = new AccountEntity();
        account.setId(dto.getAccount().getId());
        cvEntity.setAccount(account);

        cvEntity.setMartialStatus(dto.getMaritalStatus());
        cvEntity.setStatus(dto.getCvStatus());

        cvRepository.save(cvEntity);

        List<SkillDTO> skills = dto.getSkills();
        for (SkillDTO skill : skills) {
            SkillEntity entity =  new SkillEntity();
            skillMapper.DTOToEntity(skill, entity);
            entity.setId(UUID.randomUUID().toString());
            entity.setCv(cvEntity);
            skillRepository.save(entity);
        }

        List<WorkHistoryDTO> histories = dto.getWorkHistories();
        for (WorkHistoryDTO history : histories) {
            WorkHistoryEntity entity =  new WorkHistoryEntity();
            workHistoryMapper.DTOToEntity(history, entity);
            entity.setId(UUID.randomUUID().toString());
            entity.setCv(cvEntity);
            workHistoryRepository.save(entity);
        }

        List<EducationDTO> educations = dto.getEducations();
        for (EducationDTO education : educations) {
            EducationEntity entity = new EducationEntity();
            educationMapper.DTOToEntity(education, entity);
            entity.setId(UUID.randomUUID().toString());
            entity.setCv(cvEntity);
            educationRepository.save(entity);
        }

        List<CertificationDTO> certifications = dto.getCertificates();
        for (CertificationDTO certification : certifications) {
            CertificationEntity entity = new CertificationEntity();
            certificationMapper.DTOToEntity(certification, entity);
            entity.setId(UUID.randomUUID().toString());
            entity.setCv(cvEntity);
            certificationRepository.save(entity);
        }

        List<ReferenceDTO> references = dto.getReferences();
        for (ReferenceDTO reference : references) {
            ReferenceEntity entity = new ReferenceEntity();
            referenceMapper.DTOToEntity(reference, entity);
            entity.setId(UUID.randomUUID().toString());
            entity.setCv(cvEntity);
            referenceRepository.save(entity);
        }

        List<ActivityDTO> activities = dto.getActivities();
        for (ActivityDTO activity : activities) {
            ActivityEntity entity = new ActivityEntity();
            activityMapper.DTOToEntity(activity, entity);
            entity.setId(UUID.randomUUID().toString());
            entity.setCv(cvEntity);
            activityRep.save(entity);
        }
    }


    @Override
    public Integer getCountCvByEmail(String email) {
        return cvRepository.countByEmail(email);
    }

    @Override
    public Integer getCountCvByID(String id) {
        return cvRepository.countById(id);
    }


}
