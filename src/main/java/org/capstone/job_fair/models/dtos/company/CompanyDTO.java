package org.capstone.job_fair.models.dtos.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.statuses.CompanyStatus;

import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDTO {

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

    public CompanyDTO(String id) {
        this.id = id;
    }


}
