package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.models.entities.attendant.CountryEntity;
import org.capstone.job_fair.repositories.attendant.CountryRepository;
import org.capstone.job_fair.services.interfaces.attendant.cv.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public Integer getCountCountryById(String id) {
        return countryRepository.countById(id);
    }

    @Override
    public Optional<CountryEntity> getCountryById(String id) {
        return countryRepository.findById(id);
    }
}
