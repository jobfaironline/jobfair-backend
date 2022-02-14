package org.capstone.job_fair.services.impl.attendant;

import org.capstone.job_fair.repositories.attendant.ResidenceRepository;
import org.capstone.job_fair.services.interfaces.attendant.ResidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ResidenceServiceImpl implements ResidenceService {


    @Autowired
    private ResidenceRepository residenceRepository;

    @Override
    public Integer getCountResidenceById(String id) {
        return residenceRepository.countById(id);
    }
}
