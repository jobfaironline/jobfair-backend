package org.capstone.job_fair.models.dtos.job_fair.booth;

import lombok.*;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.enums.AssignmentType;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AssignmentDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private AssignmentType type;
    private Long createTime;
    private CompanyEmployeeDTO companyEmployee;
    private JobFairBoothDTO jobFairBooth;
    private Long beginTime;
    private Long endTime;
    private CompanyEmployeeDTO assigner;

}
