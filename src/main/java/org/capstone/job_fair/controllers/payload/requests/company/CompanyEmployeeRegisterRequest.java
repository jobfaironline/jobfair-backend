package org.capstone.job_fair.controllers.payload.requests.company;

import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompanyEmployeeRegisterRequest {
    @NotNull
    @EmailConstraint
    private String email;
    @NotNull
    @NotBlank
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    @XSSConstraint
    private String firstName;
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    @XSSConstraint
    private String middleName;
    @NotNull
    @NotBlank
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    @XSSConstraint
    private String lastName;
    @NotNull
    @PhoneConstraint
    private String phone;
    @NotNull
    private Gender gender;
}
