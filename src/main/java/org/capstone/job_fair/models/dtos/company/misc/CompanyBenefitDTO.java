package org.capstone.job_fair.models.dtos.company.misc;

import lombok.*;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CompanyBenefitDTO implements Serializable {
    @EqualsAndHashCode.Include
    private Integer id;
    private String description;
    private BenefitDTO benefitDTO;
    private CompanyDTO companyDTO;
}
