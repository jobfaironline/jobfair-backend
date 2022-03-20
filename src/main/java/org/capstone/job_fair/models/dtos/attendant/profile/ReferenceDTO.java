package org.capstone.job_fair.models.dtos.attendant.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferenceDTO {
    private String id;
    private String fullname;
    private String position;
    private String company;
    private String email;
    private String phone;
}
