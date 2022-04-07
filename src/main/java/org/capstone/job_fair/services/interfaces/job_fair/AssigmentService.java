package org.capstone.job_fair.services.interfaces.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.AssigmentDTO;
import org.capstone.job_fair.models.enums.AssignmentType;

public interface AssigmentService {
    AssigmentDTO assignEmployee(String employeeId, String jobFairBoothId, AssignmentType type);

    AssigmentDTO unasignEmployee(String employeeId, String jobFairBoothId);
}
