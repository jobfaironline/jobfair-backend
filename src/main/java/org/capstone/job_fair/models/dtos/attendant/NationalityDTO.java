package org.capstone.job_fair.models.dtos.attendant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NationalityDTO {
    private String id;
    private String name;
    private String description;
}
