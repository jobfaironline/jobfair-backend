package org.capstone.job_fair.models.dtos.company.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPositionDTO {
    private String id;
    private String title;
    private String description;
    private String requirements;
    private Double minSalary;
    private Double maxSalary;
    private String contactPersonName;
    private String contactEmail;
    private String companyName;
    private String companySizeId;
    private String companyAddress;
    private String companyProfile;
    private String companyLogoUrl;
}
