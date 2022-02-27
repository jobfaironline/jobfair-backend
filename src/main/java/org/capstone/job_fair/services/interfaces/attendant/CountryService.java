package org.capstone.job_fair.services.interfaces.attendant;

import org.capstone.job_fair.models.dtos.attendant.CountryDTO;

import java.util.List;

public interface CountryService {
    Integer getCountCountryById(String id);

    List<CountryDTO> getAllCountries();
}
