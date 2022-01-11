package org.capstone.job_fair.services.impl;

import org.capstone.job_fair.models.entities.account.GenderEntity;
import org.capstone.job_fair.repositories.attendant.GenderRepository;
import org.capstone.job_fair.services.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenderServiceImpl implements GenderService {

    @Autowired
    private GenderRepository genderRepository;

    @Override
    public GenderEntity findById(int id) {
        Optional<GenderEntity> opt =  genderRepository.findById(id);
        return opt.isPresent() ? opt.get() : null;
    }
}
