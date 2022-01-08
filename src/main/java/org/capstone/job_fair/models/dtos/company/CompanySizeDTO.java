package org.capstone.job_fair.models.dtos.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanySizeDTO {
    private String id;
    private String name;
    private String description;
}
