package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.ApplicationDTO;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
import org.capstone.job_fair.models.entities.company.job.RegistrationJobPositionEntity;
import org.capstone.job_fair.models.enums.Application;
import org.capstone.job_fair.repositories.attendant.cv.ApplicationRepository;
import org.capstone.job_fair.repositories.company.job.RegistrationJobPositionRepository;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.attendant.ApplicationService;
import org.capstone.job_fair.services.mappers.attendant.cd.ApplicationMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
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

    @Override
    public ApplicationDTO createNewApplication(ApplicationDTO dto) {

        //if attendantId not exist, throw error
        if (!isAccountExist(dto.getAttendantDTO().getAccount().getId())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.NOT_FOUND_ATTENDANT));
        }
        //if registration job position id not exist, throw  error
        if (!isJobPositionExist(dto.getRegistrationJobPositionDTO().getId())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.NOT_FOUND_REGISTRATION_JOB_POSITION));
        }

        ApplicationEntity entity = new ApplicationEntity();
        entity.setCreateDate(dto.getCreateDate());
        entity.setStatus(dto.getStatus());
        entity.setSummary(dto.getSummary());
        AttendantEntity attendantEntity = new AttendantEntity();
        attendantEntity.setAccountId(dto.getAttendantDTO().getAccount().getId());
        entity.setAttendant(attendantEntity);
        RegistrationJobPositionEntity registrationJobPositionEntity = new RegistrationJobPositionEntity();
        registrationJobPositionEntity.setId(dto.getRegistrationJobPositionDTO().getId());
        entity.setRegistrationJobPosition(registrationJobPositionEntity);
        entity = applicationRepository.save(entity);

        //
        ApplicationDTO resultDTO = new ApplicationDTO();
        resultDTO.setId(entity.getId());
        resultDTO.setSummary(entity.getSummary());
        resultDTO.setCreateDate(entity.getCreateDate());
        resultDTO.setStatus(entity.getStatus());
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(entity.getAttendant().getAccountId());
        AttendantDTO attendantDTO = new AttendantDTO();
        attendantDTO.setAccount(accountDTO);
        resultDTO.setAttendantDTO(attendantDTO);
        RegistrationJobPositionDTO regisDTO = new RegistrationJobPositionDTO();
        regisDTO.setId(entity.getRegistrationJobPosition().getId());
        resultDTO.setRegistrationJobPositionDTO(regisDTO);
        return resultDTO;
    }

    @Override
    public Page<ApplicationDTO> getApplicationsByCompany(String companyId, Application status, long fromTime, long toTime, int offset, int pageSize, String field) {

        if (fromTime > toTime)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INALID_TIME));
        if (offset < DataConstraint.Application.OFFSET_MIN || pageSize < DataConstraint.Application.PAGE_SIZE_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.INVALID_PAGE_NUMBER));


        return applicationRepository.findAllByCreateDateBetweenAndStatus(fromTime, toTime, status, PageRequest.of(offset, pageSize).withSort(Sort.by(field))).map(entity -> applicationMapper.toDTO(entity));

    }
}