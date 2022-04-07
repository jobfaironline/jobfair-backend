package org.capstone.job_fair.services.impl.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.AssigmentDTO;
import org.capstone.job_fair.models.entities.job_fair.AssignmentEntity;
import org.capstone.job_fair.models.enums.AssignmentType;
import org.capstone.job_fair.repositories.job_fair.AssignmentRepository;
import org.capstone.job_fair.services.interfaces.job_fair.AssigmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AssignmentServiceImpl implements AssigmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Override
    public AssigmentDTO assignEmployee(String employeeId, String jobFairBoothId, AssignmentType type) {
        return null;
    }

    @Override
    public AssigmentDTO unasignEmployee(String employeeId, String jobFairBoothId) {
        return null;
    }
}
