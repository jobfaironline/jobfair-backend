package org.capstone.job_fair.models.dtos.attendant.cv;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NationalityDTO {
    private String id;
    private String name;
    private String description;
}
