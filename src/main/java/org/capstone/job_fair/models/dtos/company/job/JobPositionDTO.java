package org.capstone.job_fair.models.dtos.company.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private String languageId;
    private String levelId;
    private String jobTypeId;
    private String locationId;
    private String comapnyId;
}
