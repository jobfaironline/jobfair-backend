package org.capstone.job_fair.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.mappers.AttendantMapper;
import org.capstone.job_fair.repositories.attendant.AttendantRepository;
import org.capstone.job_fair.services.AccountService;
import org.capstone.job_fair.services.AttendantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
@Service
@Slf4j
public class AttendantServiceImpl implements AttendantService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AttendantRepository attendantRepository;
    @Autowired
    private AttendantMapper attendantMapper;
    @Override
    public AttendantEntity getAttendantByEmail(String email) {
        System.out.println("Toi dang o day");
        Optional<AccountEntity> accountEntity = accountService.getActiveAccountByEmail(email);
        return attendantRepository.findById(accountEntity.get().getId()).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public AttendantEntity save(AttendantDTO attendantDTO) {
        return attendantRepository.findById(attendantDTO.getAccountId()).map((atd) -> {
            attendantMapper.updateAttendantMapperFromDto(attendantDTO, atd);
            return attendantRepository.save(atd);
        }).orElseThrow(NoSuchElementException::new);
    }
}
