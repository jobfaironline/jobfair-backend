package org.capstone.job_fair.services.impl.attendant;

import org.capstone.job_fair.models.dtos.attendant.CountryDTO;
import org.capstone.job_fair.models.entities.attendant.CountryEntity;
import org.capstone.job_fair.repositories.attendant.CountryRepository;
import org.capstone.job_fair.services.interfaces.attendant.CountryService;
import org.capstone.job_fair.services.mappers.attendant.CountryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public Integer getCountCountryById(String id) {
        return countryRepository.countById(id);
    }

    @Autowired
    CountryMapper countryMapper;

    @Override
    public List<CountryDTO> getAllCountries(){
        List<CountryEntity> countryEntities = countryRepository.findAll();
        return countryEntities.stream().map(countryMapper::toDTO).collect(Collectors.toList());


    }
}
