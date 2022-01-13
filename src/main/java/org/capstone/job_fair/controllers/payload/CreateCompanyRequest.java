package org.capstone.job_fair.controllers.payload;

import lombok.*;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.company.CompanySizeDTO;
import org.capstone.job_fair.models.entities.company.CompanySizeEntity;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PasswordConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CreateCompanyRequest {
    private static final int MIN_EMPLOYEE = 1;


    @NotBlank(message = "tax Id " + MessageConstant.InvalidFormat.NOT_BLANK_FORMAT)
    @Size(max = 9)
    private String taxID;

    @NotBlank(message = "Name " + MessageConstant.InvalidFormat.NOT_BLANK_FORMAT)
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

    private String url;

    @NotNull
    private String sizeId;
}
