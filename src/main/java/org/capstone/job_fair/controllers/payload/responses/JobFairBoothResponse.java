package org.capstone.job_fair.controllers.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.capstone.job_fair.models.dtos.company.JobFairBoothDTO;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class JobFairBoothResponse extends JobFairBoothDTO {
    public JobFairBoothResponse(JobFairBoothDTO parent){
        this.setId(parent.getId());
        this.setDescription(parent.getDescription());
        this.setJobFair(parent.getJobFair());
        this.setBooth(parent.getBooth());
        this.setName(parent.getName());
        this.setBoothJobPositions(parent.getBoothJobPositions());
    }
    private int visitCount;
    private String companyId;
}
