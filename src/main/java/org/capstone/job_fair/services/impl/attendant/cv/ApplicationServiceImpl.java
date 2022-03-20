package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.attendant.cv.ApplicationDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.CvDTO;
import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.repositories.attendant.cv.ApplicationRepository;
import org.capstone.job_fair.repositories.company.job.RegistrationJobPositionRepository;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.attendant.ApplicationService;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.mappers.attendant.cv.ApplicationMapper;
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

@Service
@Transactional(readOnly = true)
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private RegistrationJobPositionRepository regisJobPosRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private CvService cvService;


    private boolean isCvExist(String cvId, String attendantId) {
        Optional<CvDTO> cvDTOOptional = cvService.getByIdAndAttendantId(cvId, attendantId);
        return cvDTOOptional.isPresent();
    }

    private boolean isJobPositionExist(String registrationId) {
        return regisJobPosRepository.countById(registrationId) != 0;
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
            default:
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.UNSUPPORTED_SORT_VALUE_FOR_APPLICATION_FOR_COMPANY_ERROR));
        }
    }

    @Override
    @Transactional
    public ApplicationDTO createNewApplication(ApplicationDTO dto) {
        //if attendantId not exist, throw error
        if (!isCvExist(dto.getCvDTO().getId(), dto.getCvDTO().getAttendant().getAccount().getId())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.CV_NOT_FOUND));
        }
        //if registration job position id not exist, throw  error
        if (!isJobPositionExist(dto.getRegistrationJobPositionDTO().getId())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.NOT_FOUND_REGISTRATION_JOB_POSITION));
        }
        ApplicationEntity entity = applicationMapper.toEntity(dto);
        ApplicationEntity resultEntity = applicationRepository.save(entity);

        ApplicationDTO result = applicationMapper.toDTO(resultEntity);
        return result;
    }

    @Override
    public Page<ApplicationDTO> getAllApplicationsOfAttendantByCriteria(String attendantId, ApplicationStatus status, long fromTime, long toTime, int offset, int pageSize, String field) {

        if (fromTime > toTime)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INALID_TIME));
        if (offset < DataConstraint.Application.OFFSET_MIN || pageSize < DataConstraint.Application.PAGE_SIZE_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INVALID_PAGE_NUMBER));
        return applicationRepository.findAllByCreateDateBetweenAndStatusAndCvAttendantAccountId(fromTime, toTime, status, attendantId, PageRequest.of(offset, pageSize).withSort(Sort.by(field))).map(entity -> applicationMapper.toDTO(entity));

    }

    @Override
    public Page<ApplicationEntity> getApplicationOfCompanyByJobPositionIdAndStatus(String companyId, String jobPositionId, List<ApplicationStatus> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        validatePaging(pageSize, offset);
        sortBy = applicationForCompanySortByMapper(sortBy);
        if (statusList == null || statusList.isEmpty()) {
            statusList = Arrays.asList(ApplicationStatus.FINISHED, ApplicationStatus.PENDING, ApplicationStatus.CANCEL, ApplicationStatus.APPROVE, ApplicationStatus.REJECT);
        }
        Page<ApplicationEntity> applicationEntityPage = applicationRepository.findAllApplicationOfCompanyByJobPositionIdAndStatusIn
                (companyId, jobPositionId, statusList, PageRequest.of(offset, pageSize).withSort(direction, sortBy));
        return applicationEntityPage;
    }

    @Override
    public Page<ApplicationEntity> getApplicationOfCompanyByJobFairIdAndStatus(String companyId, String jobFairId, List<ApplicationStatus> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        validatePaging(pageSize, offset);
        sortBy = applicationForCompanySortByMapper(sortBy);
        if (statusList == null || statusList.isEmpty()) {
            statusList = Arrays.asList(ApplicationStatus.FINISHED, ApplicationStatus.PENDING, ApplicationStatus.CANCEL, ApplicationStatus.APPROVE, ApplicationStatus.REJECT);
        }

        Page<ApplicationEntity> applicationEntityPage = applicationRepository.findAllApplicationOfCompanyByJobFairIdAndStatusIn
                (companyId, jobFairId, statusList, PageRequest.of(offset, pageSize).withSort(direction, sortBy));
        return applicationEntityPage;
    }

    @Override
    public Page<ApplicationEntity> getApplicationOfCompanyByJobFairNameAndJobPositionNameAndStatus(String companyId, String jobFairName, String jobPositionName, List<ApplicationStatus> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        validatePaging(pageSize, offset);
        sortBy = applicationForCompanySortByMapper(sortBy);

        if (statusList == null || statusList.isEmpty()) {
            statusList = Arrays.asList(ApplicationStatus.FINISHED, ApplicationStatus.PENDING, ApplicationStatus.CANCEL, ApplicationStatus.APPROVE, ApplicationStatus.REJECT);
        }

        Page<ApplicationEntity> applicationEntityPage = applicationRepository.
                findAllApplicationOfCompanyByJobPositionTitleLikeAndJobFairNameLikeAndStatusIn
                        (companyId, jobPositionName, jobFairName, statusList, PageRequest.of(offset, pageSize).withSort(direction, sortBy));
        return applicationEntityPage;
    }

    @Override
    @Transactional
    public void evaluateApplication(ApplicationDTO dto) {
        String id = dto.getId();
        String companyId = dto.getRegistrationJobPositionDTO().getCompanyRegistration().getCompanyId();
        Optional<ApplicationEntity> applicationEntityOptional =
                applicationRepository.findByIdAndRegistrationJobPositionCompanyRegistrationCompanyId(id, companyId);
        if (!applicationEntityOptional.isPresent()) throw new
                IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        ApplicationEntity applicationEntity = applicationEntityOptional.get();
        if (applicationEntity.getStatus().equals(ApplicationStatus.PENDING))
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INVALID_EVALUATE_STATUS));
        if (dto.getStatus().equals(ApplicationStatus.APPROVE) && dto.getStatus().equals(ApplicationStatus.REJECT))
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INVALID_EVALUATE_STATUS));
        if (dto.getStatus().equals(ApplicationStatus.REJECT) && (dto.getEvaluateMessage() == null || dto.getEvaluateMessage().isEmpty()))
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.EVALUATE_MESSAGE_IS_EMPTY));
        applicationMapper.updateFromDTO(applicationEntity, dto);
        applicationRepository.save(applicationEntity);
    }
}
