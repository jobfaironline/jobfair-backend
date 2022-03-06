package org.capstone.job_fair.models.dtos.company;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CompanyBoothLayoutVideoDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String url;
    private String itemName;
    private String companyBoothLayoutId;
}
