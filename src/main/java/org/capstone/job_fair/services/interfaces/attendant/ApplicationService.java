package org.capstone.job_fair.services.interfaces.attendant;

import org.capstone.job_fair.models.dtos.attendant.cv.ApplicationDTO;
import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ApplicationService {
    ApplicationDTO createNewApplication(ApplicationDTO dto);

    Page<ApplicationDTO> getAllApplicationsOfAttendantByCriteria(String attendantId, ApplicationStatus status, long fromTime, long toTime, int offset, int pageSize, String field);

    Page<ApplicationEntity> getApplicationOfCompanyByJobPositionIdAndStatus(String companyId, String jobPositionId, List<ApplicationStatus> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction);

    Page<ApplicationEntity> getApplicationOfCompanyByJobFairIdAndStatus(String companyId, String jobFairId, List<ApplicationStatus> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction);

    Page<ApplicationEntity> getApplicationOfCompanyByJobFairNameAndJobPositionNameAndStatus(String companyId, String jobFairName, String jobPositionName, List<ApplicationStatus> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction);
}
