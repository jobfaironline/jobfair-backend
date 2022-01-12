package org.capstone.job_fair.services.interfaces.attendant;

import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;

public interface AttendantService{
    void createNewAccount(AttendantDTO dto);
}
