package org.capstone.job_fair.models.dtos.job_fair;

import lombok.*;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.company.JobFairBoothDTO;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AssigmentDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private Integer type;
    private CompanyEmployeeDTO companyEmployee;
    private JobFairBoothDTO jobFairBooth;
}
