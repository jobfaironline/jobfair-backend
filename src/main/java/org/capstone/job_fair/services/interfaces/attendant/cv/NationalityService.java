package org.capstone.job_fair.services.interfaces.attendant.cv;

import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.entities.attendant.NationalityEntity;

import java.util.Optional;

public interface NationalityService {
    Integer getCountNationalityById(String id);
    Optional<NationalityEntity> getNationalityById(String id);
}
