package org.capstone.job_fair.controllers.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffRegisterRequest {
    @NotNull
    @EmailConstraint
    private String email;
    @NotBlank
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    @XSSConstraint
    private String firstname;
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    @XSSConstraint
    private String middlename;
    @NotBlank
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    @XSSConstraint
    private String lastname;
    @NotNull
    @PhoneConstraint
    private String phone;
    @NotNull
    private Gender gender;
}
