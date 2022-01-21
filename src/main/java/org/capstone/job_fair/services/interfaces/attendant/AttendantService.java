package org.capstone.job_fair.services.interfaces.attendant;

import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;

import java.util.List;
import java.util.Optional;

public interface AttendantService{
    void updateAccount(AttendantDTO dto);
    Optional<AttendantDTO> getAttendantByEmail(String email);
    AttendantEntity createNewAccount(AttendantDTO attendantDTO);
    List<AttendantDTO> getAllAttendants();
}
