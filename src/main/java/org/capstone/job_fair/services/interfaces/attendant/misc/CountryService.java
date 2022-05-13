package org.capstone.job_fair.services.interfaces.attendant.misc;

import org.capstone.job_fair.models.dtos.attendant.misc.CountryDTO;

import java.util.List;
import java.util.Optional;

public interface CountryService {
    Integer getCountCountryById(int id);

    Optional<CountryDTO> findById(int id);

    List<CountryDTO> getAll();

    CountryDTO delete(int id);

    CountryDTO create(CountryDTO dto);

    CountryDTO update(CountryDTO dto);
}
