package org.capstone.job_fair.services.interfaces.attendant;

import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;

public interface AttendantService{
    void createNewAccount(AttendantDTO dto);
    AttendantEntity getAttendantByEmail(String email);
    AttendantEntity save(AttendantDTO attendantDTO);
}
