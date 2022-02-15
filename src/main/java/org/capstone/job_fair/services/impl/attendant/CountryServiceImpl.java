package org.capstone.job_fair.services.impl.attendant;

import org.capstone.job_fair.repositories.attendant.CountryRepository;
import org.capstone.job_fair.services.interfaces.attendant.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public Integer getCountCountryById(String id) {
        return countryRepository.countById(id);
    }
}
