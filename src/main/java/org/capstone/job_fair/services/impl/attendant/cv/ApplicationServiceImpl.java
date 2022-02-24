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
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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

        //call new application entity
        ApplicationEntity entity = new ApplicationEntity();
        //set create date + status + summary
        entity.setCreateDate(dto.getCreateDate());
        entity.setStatus(dto.getStatus());
        entity.setSummary(dto.getSummary());
        //call new attendant entity + set accountId to it
        AttendantEntity attendantEntity = new AttendantEntity();
        attendantEntity.setAccountId(dto.getAttendantDTO().getAccount().getId());
        //set attendant entity for application entity
        entity.setAttendant(attendantEntity);
        //call new registration job position entity
        RegistrationJobPositionEntity registrationJobPositionEntity = new RegistrationJobPositionEntity();
        //then set Id to it
        registrationJobPositionEntity.setId(dto.getRegistrationJobPositionDTO().getId());
        //set registration job position entity to application entity
        entity.setRegistrationJobPosition(registrationJobPositionEntity);
        //finally, save
        entity = cvRepository.save(entity);

        //Return result DTO
        ApplicationDTO resultDTO = new ApplicationDTO();
        //set Id
        resultDTO.setId(entity.getId());
        //set summary
        resultDTO.setSummary(entity.getSummary());
        //set create date
        resultDTO.setCreateDate(entity.getCreateDate());
        //set status
        resultDTO.setStatus(entity.getStatus());
        //call new AccountDTO, then setId for it
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(entity.getAttendant().getAccountId());
        //call new AttendantDTO, then set accountDTO for it
        AttendantDTO attendantDTO = new AttendantDTO();
        attendantDTO.setAccount(accountDTO);
        //set attendantDTO to resultDTO
        resultDTO.setAttendantDTO(attendantDTO);
        //call new registration job position DTO, then set it to resultDTO
        RegistrationJobPositionDTO regisDTO = new RegistrationJobPositionDTO();
        regisDTO.setId(entity.getRegistrationJobPosition().getId());
        resultDTO.setRegistrationJobPositionDTO(regisDTO);
        //return
        return resultDTO;
    }
}
