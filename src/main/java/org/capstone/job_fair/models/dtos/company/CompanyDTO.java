package org.capstone.job_fair.models.dtos.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    private String taxId;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String websiteUrl;
}
