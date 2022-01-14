package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.entities.attendant.NationalityEntity;
import org.capstone.job_fair.repositories.attendant.NationalityRepository;
import org.capstone.job_fair.services.interfaces.attendant.cv.NationalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NationalityServiceImpl implements NationalityService {

    @Autowired
    private NationalityRepository nationalityRepository;

    @Override
    public Integer getCountNationalityById(String id) {
        return nationalityRepository.countById(id);
    }

    @Override
    public Optional<NationalityEntity> getNationalityById(String id) {
        return nationalityRepository.findById(id);
    }
}
