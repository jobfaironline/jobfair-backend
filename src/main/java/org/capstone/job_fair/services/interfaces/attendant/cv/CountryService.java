package org.capstone.job_fair.services.interfaces.attendant.cv;

import org.capstone.job_fair.models.entities.attendant.CountryEntity;

import java.util.Optional;

public interface CountryService {
    Integer getCountCountryById(String id);

    Optional<CountryEntity> getCountryById(String id);
}
