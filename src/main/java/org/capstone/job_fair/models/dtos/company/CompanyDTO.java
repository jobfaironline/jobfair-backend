package org.capstone.job_fair.models.dtos.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    private int employeeMaxNum;
    private String websiteUrl;
    private String sizeId;
}
