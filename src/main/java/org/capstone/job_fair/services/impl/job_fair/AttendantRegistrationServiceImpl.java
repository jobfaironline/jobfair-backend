package org.capstone.job_fair.services.impl.job_fair;

import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.job_fair.AttendantRegistrationDTO;
import org.capstone.job_fair.models.entities.job_fair.AttendantRegistrationEntity;
import org.capstone.job_fair.repositories.job_fair.AttendantRegistrationRepository;
import org.capstone.job_fair.services.interfaces.job_fair.AttendantRegistrationService;
import org.capstone.job_fair.services.mappers.job_fair.AttendantRegistrationMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;


@Service
public class AttendantRegistrationServiceImpl implements AttendantRegistrationService {

    @Autowired
    private AttendantRegistrationRepository attendantRegistrationRepository;

    @Autowired
    private AttendantRegistrationMapper attendantRegistrationMapper;

    @Override
    public Page<AttendantRegistrationDTO> getAllRegisteredJobFair(int pageSize, int offset) {
        if (pageSize < DataConstraint.Paging.PAGE_SIZE_MIN || offset < DataConstraint.Paging.OFFSET_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Paging.INVALID_PAGE_NUMBER));
        Page<AttendantRegistrationEntity> attendantRegistrationEntities = attendantRegistrationRepository.findAll(PageRequest.of(offset, pageSize));
        return attendantRegistrationEntities.map(entity -> attendantRegistrationMapper.toDTO(entity));
    }

    @Override
    public AttendantRegistrationDTO visitJobFair(String userId, String jobFairId) {
        Optional<AttendantRegistrationEntity> registrationOpt = attendantRegistrationRepository.findByAttendantIdAndJobFairId(userId, jobFairId);
        AttendantRegistrationEntity registration = registrationOpt.orElseGet(() -> this.attendantRegistrationMapper.toEntity(this.registerJobFairAttendant(userId, jobFairId)));
        registration.setIsVisit(true);
        registration.setVisitTime(new Date().getTime());
        return attendantRegistrationMapper.toDTO(attendantRegistrationRepository.save(registration));
    }

    @Override
    public AttendantRegistrationDTO registerJobFairAttendant(String userId, String jobFairId) {
        AttendantRegistrationEntity registration = new AttendantRegistrationEntity();
        registration.setAttendantId(userId);
        registration.setJobFairId(jobFairId);
        Long now = new Date().getTime();
        registration.setCreateTime(now);
        registration.setIsVisit(false);
        return attendantRegistrationMapper.toDTO(attendantRegistrationRepository.save(registration));
    }
}
