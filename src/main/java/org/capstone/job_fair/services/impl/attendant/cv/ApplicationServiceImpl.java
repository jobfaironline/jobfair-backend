package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.attendant.application.ApplicationDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.CvDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.capstone.job_fair.models.entities.company.job.BoothJobPositionEntity;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.models.enums.TestStatus;
import org.capstone.job_fair.repositories.attendant.cv.ApplicationRepository;
import org.capstone.job_fair.repositories.attendant.cv.CvRepository;
import org.capstone.job_fair.repositories.company.job.BoothJobPositionRepository;
import org.capstone.job_fair.services.interfaces.attendant.ApplicationService;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.interfaces.attendant.cv.test.QuizService;
import org.capstone.job_fair.services.mappers.attendant.application.*;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private BoothJobPositionRepository regisJobPosRepository;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private CvRepository cvRepository;

    @Autowired
    private ApplicationActivityMapper applicationActivityMapper;

    @Autowired
    private ApplicationCertificationMapper applicationCertificationMapper;

    @Autowired
    private ApplicationEducationMapper applicationEducationMapper;

    @Autowired
    private ApplicationReferenceMapper applicationReferenceMapper;

    @Autowired
    private ApplicationSkillMapper applicationSkillMapper;

    @Autowired
    private CvService cvService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private ApplicationWorkHistoryMapper applicationWorkHistoryMapper;


    private boolean isCvExist(String cvId, String attendantId) {
        Optional<CvDTO> cvDTOOptional = cvService.getByIdAndAttendantId(cvId, attendantId);
        return cvDTOOptional.isPresent();
    }


    private TestStatus getTestStatus(String boothJobPositionId) {
        Optional<BoothJobPositionEntity> boothJobPositionEntityOptional = regisJobPosRepository.findById(boothJobPositionId);
        if (!boothJobPositionEntityOptional.isPresent()) {
            return null;
        }
        if (boothJobPositionEntityOptional.get().getIsHaveTest()) return TestStatus.NOT_TAKEN;
        return TestStatus.NO_TEST;
    }

    private void validatePaging(int pageSize, int offset) {
        if (offset < DataConstraint.Paging.OFFSET_MIN || pageSize < DataConstraint.Paging.PAGE_SIZE_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INVALID_PAGE_NUMBER));
    }


    private String applicationForCompanySortByMapper(String sortBy) {
        switch (sortBy) {
            case "appliedDate":
                return "createDate";
            case "jobPositionTitle":
                return "registrationJobPosition.title";
            case "jobFairName":
                return "registrationJobPosition.companyRegistration.jobFairEntity.name";
            case "evaluateDate":
                return "evaluateDate";
            default:
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.UNSUPPORTED_SORT_VALUE_FOR_APPLICATION_FOR_COMPANY_ERROR));
        }
    }

    @Override
    @Transactional
    public ApplicationDTO createNewApplication(ApplicationDTO dto) {
        //if attendantId not exist, throw error
        Optional<CvEntity> cvOptional = cvRepository.findByIdAndAttendantAccountId(dto.getOriginCvId(), dto.getAttendant().getAccount().getId());
        if (!cvOptional.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.CV_NOT_FOUND));
        }
        CvEntity cvEntity = cvOptional.get();
        //if registration job position id not exist, throw  error
        TestStatus testStatus = getTestStatus(dto.getBoothJobPositionDTO().getId());
        if (testStatus == null) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.NOT_FOUND_REGISTRATION_JOB_POSITION));
        }
        dto.setTestStatus(testStatus);
        ApplicationEntity entity = applicationMapper.toEntity(dto);
        entity.setEmail(cvEntity.getEmail());
        entity.setPhone(cvEntity.getPhone());
        entity.setYearOfExp(cvEntity.getYearOfExp());
        entity.setJobTitle(cvEntity.getJobTitle());
        entity.setJobLevel(cvEntity.getJobLevel());
        entity.setActivities(cvEntity.getActivities().stream().map(applicationActivityMapper::toEntity).collect(Collectors.toList()));
        entity.setCertifications(cvEntity.getCertifications().stream().map(applicationCertificationMapper::toEntity).collect(Collectors.toList()));
        entity.setEducations(cvEntity.getEducations().stream().map(applicationEducationMapper::toEntity).collect(Collectors.toList()));
        entity.setReferences(cvEntity.getReferences().stream().map(applicationReferenceMapper::toEntity).collect(Collectors.toList()));
        entity.setSkills(cvEntity.getSkills().stream().map(applicationSkillMapper::toEntity).collect(Collectors.toList()));
        entity.setWorkHistories(cvEntity.getWorkHistories().stream().map(applicationWorkHistoryMapper::toEntity).collect(Collectors.toList()));

        ApplicationEntity resultEntity = applicationRepository.save(entity);


        ApplicationDTO result = applicationMapper.toDTO(resultEntity);
        return result;
    }

    @Override
    public Page<ApplicationEntity> getAllApplicationsOfAttendantByCriteria(String userId, String jobFairName, String jobPositionName, List<ApplicationStatus> statusList, Long fromTime, Long toTime, int offset, int pageSize, String sortBy, Sort.Direction direction) {
        validatePaging(pageSize, offset);
        if (statusList == null || statusList.isEmpty())
            statusList = Arrays.asList(ApplicationStatus.FINISHED, ApplicationStatus.DRAFT, ApplicationStatus.DELETED, ApplicationStatus.PENDING, ApplicationStatus.CANCEL, ApplicationStatus.APPROVE, ApplicationStatus.REJECT);
        if (fromTime > toTime)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INALID_TIME));
        return applicationRepository.findAllApplicationOfAttendantByCriteria(userId, fromTime, toTime, statusList, jobPositionName, jobFairName, PageRequest.of(offset, pageSize).withSort(direction, sortBy));

    }

    @Override
    @Transactional
    public void submitApplication(String applicationId, String userId) {
        Optional<ApplicationEntity> entityOptional = applicationRepository.findById(applicationId);
        if (!entityOptional.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        }
        ApplicationEntity entity = entityOptional.get();
        if (!entity.getAttendant().getAccount().getId().equals(userId)) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Attendant.ATTENDANT_MISMATCH));
        }

        List<ApplicationEntity> result = applicationRepository.findByOriginCvIdAndBoothJobPositionIdAndStatusIn(
                entity.getOriginCvId(),
                entity.getBoothJobPosition().getId(),
                Arrays.asList(ApplicationStatus.APPROVE, ApplicationStatus.PENDING, ApplicationStatus.REJECT));

        if (!result.isEmpty()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.ALREADY_APPLY_CV));
        }

        entity.setStatus(ApplicationStatus.PENDING);
        applicationRepository.save(entity);
    }

    @Override
    public Optional<ApplicationDTO> getApplicationById(String applicationId, String userId) {
        return applicationRepository.findByIdAndAttendantAccountId(applicationId, userId).map(applicationMapper::toDTO);
    }

    @Override
    public Page<ApplicationEntity> getApplicationOfCompanyByJobPositionIdAndStatus(String companyId, String jobPositionId, List<ApplicationStatus> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        validatePaging(pageSize, offset);
        sortBy = applicationForCompanySortByMapper(sortBy);
        if (statusList == null || statusList.isEmpty()) {
            statusList = Arrays.asList(ApplicationStatus.FINISHED, ApplicationStatus.PENDING, ApplicationStatus.CANCEL, ApplicationStatus.APPROVE, ApplicationStatus.REJECT);
        }
        Page<ApplicationEntity> applicationEntityPage = applicationRepository.findAllApplicationOfCompanyByJobPositionIdAndStatusIn(companyId, jobPositionId, statusList, PageRequest.of(offset, pageSize).withSort(direction, sortBy));
        return applicationEntityPage;
    }

    @Override
    public Page<ApplicationEntity> getApplicationOfCompanyByJobFairIdAndStatus(String companyId, String jobFairId, List<ApplicationStatus> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        validatePaging(pageSize, offset);
        sortBy = applicationForCompanySortByMapper(sortBy);
        if (statusList == null || statusList.isEmpty()) {
            statusList = Arrays.asList(ApplicationStatus.FINISHED, ApplicationStatus.PENDING, ApplicationStatus.CANCEL, ApplicationStatus.APPROVE, ApplicationStatus.REJECT);
        }

        Page<ApplicationEntity> applicationEntityPage = applicationRepository.findAllApplicationOfCompanyByJobFairIdAndStatusIn(companyId, jobFairId, statusList, PageRequest.of(offset, pageSize).withSort(direction, sortBy));
        return applicationEntityPage;
    }

    @Override
    public Page<ApplicationEntity> getApplicationOfCompanyByJobFairNameAndJobPositionNameAndStatus(String companyId, String jobFairName, String jobPositionName, List<ApplicationStatus> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        validatePaging(pageSize, offset);
        sortBy = applicationForCompanySortByMapper(sortBy);

        if (statusList == null || statusList.isEmpty()) {
            statusList = Arrays.asList(ApplicationStatus.FINISHED, ApplicationStatus.PENDING, ApplicationStatus.CANCEL, ApplicationStatus.APPROVE, ApplicationStatus.REJECT);
        }

        Page<ApplicationEntity> applicationEntityPage = applicationRepository.findAllApplicationOfCompanyByJobPositionTitleLikeAndJobFairNameLikeAndStatusIn(companyId, jobPositionName, jobFairName, statusList, PageRequest.of(offset, pageSize).withSort(direction, sortBy));
        return applicationEntityPage;
    }

    @Override
    public Optional<ApplicationEntity> getApplicationWithGeneralDataByIdOfCompany(String companyId, String applicationId) {
        Optional<ApplicationEntity> applicationEntityOptional = applicationRepository.findByIdAndCompanyId(applicationId, companyId);
        if (!applicationEntityOptional.isPresent()) return Optional.empty();
        return applicationEntityOptional;
    }

    @Override
    @Transactional
    public void evaluateApplication(ApplicationDTO dto) {
        String id = dto.getId();
        String companyId = dto.getBoothJobPositionDTO().getJobFairBooth().getJobFair().getCompany().getId();
        Optional<ApplicationEntity> applicationEntityOptional = applicationRepository.findByIdAndCompanyId(id, companyId);
        if (!applicationEntityOptional.isPresent())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        ApplicationEntity applicationEntity = applicationEntityOptional.get();
        if (!applicationEntity.getStatus().equals(ApplicationStatus.PENDING))
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INVALID_EVALUATE_STATUS));
        if (!dto.getStatus().equals(ApplicationStatus.APPROVE) && !dto.getStatus().equals(ApplicationStatus.REJECT))
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INVALID_EVALUATE_STATUS));
        if (dto.getStatus().equals(ApplicationStatus.REJECT) && (dto.getEvaluateMessage() == null || dto.getEvaluateMessage().isEmpty()))
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.EVALUATE_MESSAGE_IS_EMPTY));
        applicationMapper.updateFromDTO(applicationEntity, dto);
        applicationRepository.save(applicationEntity);
    }
}
