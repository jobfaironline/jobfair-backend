package org.capstone.job_fair.services;

import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;

import java.util.Optional;

public interface AttendantService {
    AttendantEntity getAttendantByEmail(String email);
    AttendantEntity save(AttendantDTO attendantDTO);
}
