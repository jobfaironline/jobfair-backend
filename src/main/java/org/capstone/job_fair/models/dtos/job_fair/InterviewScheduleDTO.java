package org.capstone.job_fair.models.dtos.job_fair;

import lombok.Data;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.statuses.InterviewStatus;

@Data
public class InterviewScheduleDTO {
    private String id;
    private Long endTime;
    private Long beginTime;
    private String name;
    private String description;
    private InterviewStatus status;
    private String url;
    private String interviewerId;
    private String attendantId;
}
