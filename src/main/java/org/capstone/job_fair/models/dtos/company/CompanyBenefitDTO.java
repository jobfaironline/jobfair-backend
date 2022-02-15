package org.capstone.job_fair.models.dtos.company;

import lombok.*;

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
