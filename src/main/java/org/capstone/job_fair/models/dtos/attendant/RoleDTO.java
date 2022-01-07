package org.capstone.job_fair.models.dtos.attendant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.constants.RoleName;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private String id;
    private RoleName name;
    private String description;
}
