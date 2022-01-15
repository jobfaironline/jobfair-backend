package org.capstone.job_fair.controllers.payload.requests;

import lombok.*;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;
import org.capstone.job_fair.validators.XSSConstraint;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateCompanyRequest {


    @NotNull
    private String id;

    @Size(max = DataConstraint.Company.NAME_LENGTH)
    @NotEmpty
    @XSSConstraint
    private String name;

    @Size(max = DataConstraint.Company.ADDRESS_LENGTH)
    @NotEmpty
    @XSSConstraint
    private String address;

    @PhoneConstraint
    private String phone;

    @EmailConstraint
    private String email;

    @Min(value = DataConstraint.Company.MIN_EMPLOYEE)
    private Integer employeeMaxNum;

    private String taxId;

    @NotEmpty
    @XSSConstraint
    private String url;

    private String sizeId;
}
