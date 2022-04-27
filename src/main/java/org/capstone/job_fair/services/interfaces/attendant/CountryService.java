package org.capstone.job_fair.services.interfaces.attendant;

import org.capstone.job_fair.models.dtos.attendant.CountryDTO;

import java.util.List;
import java.util.Optional;

public interface CountryService {
    Integer getCountCountryById(int id);

    List<CountryDTO> getAllCountries();

    Optional<CountryDTO> findById(int id);

    List<CountryDTO> getAll();

    CountryDTO delete(int id);

    CountryDTO create(CountryDTO dto);

    CountryDTO update(CountryDTO dto);
}
