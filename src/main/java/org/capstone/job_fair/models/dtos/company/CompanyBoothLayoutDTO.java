package org.capstone.job_fair.models.dtos.company;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CompanyBoothLayoutDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private Integer version;
    private Long createDate;
    private CompanyBoothDTO companyBooth;
}
