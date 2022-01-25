package org.capstone.job_fair.controllers.payload.requests;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PromoteEmployeeToCompanyManagerRequest {
    @NotNull
    private String employeeId;
    @NotNull
    private String managerId;
}
