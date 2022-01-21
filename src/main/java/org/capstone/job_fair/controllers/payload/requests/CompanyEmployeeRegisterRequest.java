package org.capstone.job_fair.controllers.payload.requests;

import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.constraints.Size;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompanyEmployeeRegisterRequest {
    private String companyId;
    @EmailConstraint
    private String email;
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    @XSSConstraint
    private String firstName;
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    @XSSConstraint
    private String middleName;
    @Size(max = DataConstraint.Account.NAME_LENGTH)
    @XSSConstraint
    private String lastName;
    @PhoneConstraint
    private String phone;
    private Gender gender;
}
