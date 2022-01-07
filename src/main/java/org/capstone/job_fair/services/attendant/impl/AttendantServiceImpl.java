package org.capstone.job_fair.services.attendant.impl;

import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.repositories.attendant.AttendantRepository;
import org.capstone.job_fair.services.attendant.AttendantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttendantServiceImpl implements AttendantService {

    @Autowired
    private AttendantRepository attendantRepository;

    @Override
    public List<AttendantEntity> getAllAccounts() {
        return attendantRepository.findAll(); //can be sorted before return...
    }

    public Optional<AttendantEntity> getActiveAccountByEmail(String email){
        return attendantRepository.findAccountByEmailAndStatusNot(email, AccountConstant.ACTIVE);
    }
}
