package org.capstone.job_fair.services.interfaces.attendant;

import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;

import java.util.List;
import java.util.Optional;

public interface AttendantService {
    void updateAccount(AttendantDTO dto);

    AttendantDTO createNewAccount(AttendantDTO attendantDTO);

    List<AttendantDTO> getAllAttendants();

    Optional<AttendantDTO> getAttendantById(String id);

}
