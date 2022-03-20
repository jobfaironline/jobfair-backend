package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.responses.ApplicationForCompanyResponse;
import org.capstone.job_fair.models.dtos.attendant.cv.ApplicationDTO;
import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
import org.capstone.job_fair.models.enums.Application;
import org.capstone.job_fair.repositories.attendant.cv.ApplicationRepository;
import org.capstone.job_fair.repositories.company.job.RegistrationJobPositionRepository;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.attendant.ApplicationService;
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


    private boolean isAccountExist(String accountId) {
        return accountService.getCountActiveAccountById(accountId) != 0;
    }

    private boolean isJobPositionExist(String registrationId) {
        return regisJobPosRepository.countById(registrationId) != 0;
    }

    private void validatePaging(int pageSize, int offset) {
        if (offset < DataConstraint.Paging.OFFSET_MIN || pageSize < DataConstraint.Paging.PAGE_SIZE_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INVALID_PAGE_NUMBER));
    }


    private String ApplicationForCompanySortByMapper(String sortBy) {
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
    public ApplicationDTO createNewApplication(ApplicationDTO dto) {

        return null;
    }

    @Override
    public Page<ApplicationDTO> getAllApplicationsOfAttendantByCriteria(String attendantId, Application status, long fromTime, long toTime, int offset, int pageSize, String field) {

        if (fromTime > toTime)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INALID_TIME));
        if (offset < DataConstraint.Application.OFFSET_MIN || pageSize < DataConstraint.Application.PAGE_SIZE_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INVALID_PAGE_NUMBER));
        return applicationRepository.findAllByCreateDateBetweenAndStatusAndCvAttendantAccountId(fromTime, toTime, status, attendantId, PageRequest.of(offset, pageSize).withSort(Sort.by(field))).map(entity -> applicationMapper.toDTO(entity));

    }

    @Override
    public Page<ApplicationForCompanyResponse> getApplicationOfCompanyByJobPositionIdAndStatus(String companyId, String jobPositionId, List<Application> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        validatePaging(pageSize, offset);
        sortBy = ApplicationForCompanySortByMapper(sortBy);
        if (statusList == null || statusList.isEmpty()) {
            statusList = Arrays.asList(Application.FINISHED, Application.PENDING, Application.CANCEL, Application.APPROVE, Application.REJECT);
        }
        Page<ApplicationEntity> applicationEntityPage = applicationRepository.findAllApplicationOfCompanyByJobPositionIdAndStatusIn
                (companyId, jobPositionId, statusList, PageRequest.of(offset, pageSize).withSort(direction, sortBy));
        Page<ApplicationForCompanyResponse> responses = applicationEntityPage.map(entity -> applicationMapper.toApplicationForCompanyResponse(entity));
        return responses;
    }

    @Override
    public Page<ApplicationForCompanyResponse> getApplicationOfCompanyByJobFairIdAndStatus(String companyId, String jobFairId, List<Application> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        validatePaging(pageSize, offset);
        sortBy = ApplicationForCompanySortByMapper(sortBy);
        if (statusList == null || statusList.isEmpty()) {
            statusList = Arrays.asList(Application.FINISHED, Application.PENDING, Application.CANCEL, Application.APPROVE, Application.REJECT);
        }

        Page<ApplicationEntity> applicationEntityPage = applicationRepository.findAllApplicationOfCompanyByJobFairIdAndStatusIn
                (companyId, jobFairId, statusList, PageRequest.of(offset, pageSize).withSort(direction, sortBy));
        Page<ApplicationForCompanyResponse> responses = applicationEntityPage.map(entity -> applicationMapper.toApplicationForCompanyResponse(entity));
        return responses;
    }

    @Override
    public Page<ApplicationForCompanyResponse> getApplicationOfCompanyByJobFairNameAndJobPositionNameAndStatus(String companyId, String jobFairName, String jobPositionName, List<Application> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        validatePaging(pageSize, offset);
        sortBy = ApplicationForCompanySortByMapper(sortBy);

        if (statusList == null || statusList.isEmpty()) {
            statusList = Arrays.asList(Application.FINISHED, Application.PENDING, Application.CANCEL, Application.APPROVE, Application.REJECT);
        }

        Page<ApplicationEntity> applicationEntityPage = applicationRepository.
                findAllApplicationOfCompanyByJobPositionTitleLikeAndJobFairNameLikeAndStatusIn
                        (companyId, jobPositionName, jobFairName, statusList, PageRequest.of(offset, pageSize).withSort(direction, sortBy));
        Page<ApplicationForCompanyResponse> responses = applicationEntityPage.map(entity -> applicationMapper.toApplicationForCompanyResponse(entity));
        return responses;
    }
}
