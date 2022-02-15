package org.capstone.job_fair.services.impl.account;

import org.capstone.job_fair.models.entities.account.GenderEntity;
import org.capstone.job_fair.repositories.account.GenderRepository;
import org.capstone.job_fair.services.interfaces.account.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class GenderServiceImpl implements GenderService {

    @Autowired
    private GenderRepository genderRepository;

    @Override
    public Optional<GenderEntity> findById(int id) {
        return genderRepository.findById(id);
    }
}
