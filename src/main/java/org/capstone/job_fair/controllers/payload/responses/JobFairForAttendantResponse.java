package org.capstone.job_fair.controllers.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobFairForAttendantResponse extends JobFairDTO {
    public JobFairForAttendantResponse(JobFairDTO parent) {
        this.setId(parent.getId());
        this.setCreateTime(parent.getCreateTime());
        this.setDecorateStartTime(parent.getDecorateStartTime());
        this.setDecorateEndTime(parent.getDecorateEndTime());
        this.setPublicStartTime(parent.getPublicStartTime());
        this.setPublicEndTime(parent.getPublicEndTime());
        this.setName(parent.getName());
        this.setDescription(parent.getDescription());
        this.setTargetAttendant(parent.getTargetAttendant());
        this.setThumbnailUrl(parent.getThumbnailUrl());
        this.setStatus(parent.getStatus());
        this.setCancelReason(parent.getCancelReason());
        this.setHostName(parent.getHostName());
        this.setCompany(parent.getCompany());
    }

    private int visitCount;
}
