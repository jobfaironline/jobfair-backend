package org.capstone.job_fair.models.dtos.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


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
    private Integer sizeId;
    private List<SubCategoryDTO> subCategoryDTOs;
    private List<BenefitDTO> benefitDTOs;
    private List<MediaDTO> mediaDTOS;
}
