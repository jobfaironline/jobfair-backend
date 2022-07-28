package org.capstone.job_fair.models.dtos.job_fair.booth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobFairAssignmentDTO {
    private JobFairDTO jobFair;
    private List<AssignmentDTO> assignments;
}
