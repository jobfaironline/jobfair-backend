package org.capstone.job_fair.services.interfaces.attendant;

import org.capstone.job_fair.controllers.payload.responses.ApplicationForCompanyResponse;
import org.capstone.job_fair.models.dtos.attendant.cv.ApplicationDTO;
import org.capstone.job_fair.models.enums.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ApplicationService {
    ApplicationDTO createNewApplication(ApplicationDTO dto);

    Page<ApplicationDTO> getAllApplicationsOfAttendantByCriteria(String attendantId, Application status, long fromTime, long toTime, int offset, int pageSize, String field);

    Page<ApplicationForCompanyResponse> getApplicationOfCompanyByJobPositionIdAndStatus(String companyId, String jobPositionId, List<Application> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction);

    Page<ApplicationForCompanyResponse> getApplicationOfCompanyByJobFairIdAndStatus(String companyId, String jobFairId, List<Application> statusList, int pageSize, int offset);

    Page<ApplicationForCompanyResponse> getApplicationOfCompanyByJobFairNameAndJobPositionNameAndStatus(String companyId, String jobFairName, String jobPositionName, List<Application> statusList, int pageSize, int offset, String sortBy, Sort.Direction direction);
}
