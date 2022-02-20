package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.ApplicationDTO;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
import org.capstone.job_fair.models.entities.company.job.RegistrationJobPositionEntity;
import org.capstone.job_fair.repositories.attendant.AttendantRepository;
import org.capstone.job_fair.repositories.attendant.cv.CvRepository;
import org.capstone.job_fair.repositories.company.job.RegistrationJobPositionRepository;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.attendant.ApplicationService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private CvRepository cvRepository;

    @Autowired
    private RegistrationJobPositionRepository regisJobPosRepository;

    @Autowired
    private AccountService accountService;


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
        entity = cvRepository.save(entity);

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
}
