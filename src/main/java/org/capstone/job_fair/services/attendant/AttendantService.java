package org.capstone.job_fair.services.attendant;

import org.capstone.job_fair.models.entities.attendant.AttendantEntity;

import java.util.List;
import java.util.Optional;

public interface AttendantService {
    List<AttendantEntity> getAllAccounts();

    Optional<AttendantEntity> getActiveAccountByEmail(String email);
}
