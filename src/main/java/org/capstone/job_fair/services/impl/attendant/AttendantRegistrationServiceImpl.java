package org.capstone.job_fair.services.impl.attendant;

import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.attendant.AttendantRegistrationDTO;
import org.capstone.job_fair.models.entities.attendant.AttendantRegistrationEntity;
import org.capstone.job_fair.repositories.attendant.AttendantRegistrationRepository;
import org.capstone.job_fair.services.interfaces.attendant.AttendantRegistrationService;
import org.capstone.job_fair.services.mappers.attendant.AttendantRegistrationMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class AttendantRegistrationServiceImpl implements AttendantRegistrationService {

    @Autowired
    AttendantRegistrationRepository attendantRegistrationRepository;

    @Autowired
    AttendantRegistrationMapper attendantRegistrationMapper;

    @Override
    public Page<AttendantRegistrationDTO> getAllRegisteredJobFair(int pageSize, int offset) {
        if (pageSize < DataConstraint.Paging.PAGE_SIZE_MIN || offset < DataConstraint.Paging.OFFSET_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Paging.INVALID_PAGE_NUMBER));
        Page<AttendantRegistrationEntity> attendantRegistrationEntities = attendantRegistrationRepository.findAll(PageRequest.of(offset, pageSize));
        return attendantRegistrationEntities.map(entity -> attendantRegistrationMapper.toDTO(entity));
    }
}
