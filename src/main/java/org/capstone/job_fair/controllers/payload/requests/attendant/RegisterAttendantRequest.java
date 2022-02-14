package org.capstone.job_fair.controllers.payload.requests.attendant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PasswordConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class RegisterAttendantRequest {

    @XSSConstraint
    @NotEmpty
    @PasswordConstraint
    private String password;
    @EmailConstraint
    private String email;
    @PhoneConstraint
    private String phone;
    @NotEmpty
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    @XSSConstraint
    @NotEmpty
    private String firstname;
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    @XSSConstraint
    @NotEmpty
    private String lastname;
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    @XSSConstraint
    @NotEmpty
    private String middlename;
    private Gender gender;
}
