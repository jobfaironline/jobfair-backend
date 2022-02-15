package org.capstone.job_fair.models.dtos.company;

import lombok.*;
import org.capstone.job_fair.models.statuses.CompanyStatus;

import java.util.List;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CompanyDTO {
    @EqualsAndHashCode.Include
    private String id;
    private String taxId;
    private String name;
    private String address;
    private String phone;
    private String email;
    private Integer employeeMaxNum;
    private String websiteUrl;
    private CompanyStatus status;
    private Integer sizeId;
    private Set<SubCategoryDTO> subCategoryDTOs;
    private Set<CompanyBenefitDTO> companyBenefitDTOS;
    private List<MediaDTO> mediaDTOS;
    private String companyLogoURL;
    private String companyDescription;

    public CompanyDTO(String id) {
        this.id = id;
    }


}
