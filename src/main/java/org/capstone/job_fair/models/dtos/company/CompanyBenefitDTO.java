package org.capstone.job_fair.models.dtos.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyBenefitDTO implements Serializable {
    private Integer id;
    private String description;
    private BenefitDTO benefitDTO;
    private CompanyDTO companyDTO;
}
