package org.capstone.job_fair.controllers.payload.requests.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.NameConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCompanyEmployeeCSVRequest {
    @NotNull
    @NameConstraint
    @XSSConstraint
    @NotBlank
    private String firstName;
    @NotNull
    @NameConstraint
    @XSSConstraint
    @NotBlank
    private String middleName;
    @NotNull
    @NameConstraint
    @XSSConstraint
    @NotBlank
    private String lastName;
    @NotNull
    @NameConstraint
    @XSSConstraint
    @NotBlank
    private String department;
    @NotNull
    @NotBlank
    private String employeeId;
    @NotNull
    @EmailConstraint
    private String email;
}
