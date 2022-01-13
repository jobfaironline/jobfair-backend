package org.capstone.job_fair.controllers.payload;

import lombok.*;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateCompanyRequest {
    private static final int MIN_EMPLOYEE = 1;

    @NotNull
    private String id;

    @Size(max = 1000)
    private String name;

    @Size(max = 1000)
    private String address;

    @PhoneConstraint
    private String phone;

    @EmailConstraint
    private String email;

    @Min(value = MIN_EMPLOYEE , message = MessageConstant.InvalidFormat.MIN_EMPLOYEE_INVALID)
    private Integer employeeMaxNum;

    private String taxId;

    private String url;

    private String sizeId;
}
