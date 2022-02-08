package org.capstone.job_fair.models.dtos.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyRegistrationDTO implements Serializable {
    private String id;
    private long createDate;
    private String description;
    private String jobFairId;
    private String companyId;
}
