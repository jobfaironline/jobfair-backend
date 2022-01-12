package org.capstone.job_fair.controllers.payload;

import lombok.*;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.validators.EmailConstraint;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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

    @NotBlank(message = "Name " + MessageConstant.InvalidFormat.NOT_BLANK_FORMAT)
    @Size(max = 1000)
    private String name;

    @NotBlank(message = "Address  " + MessageConstant.InvalidFormat.NOT_BLANK_FORMAT)
    @Size(max = 1000)
    private String address;

    @NotBlank(message = "Phone " + MessageConstant.InvalidFormat.NOT_BLANK_FORMAT)
    @Size(max = 11)
    private String phone;

    @EmailConstraint
    private String email;

    @Min(value = MIN_EMPLOYEE , message = MessageConstant.InvalidFormat.MIN_EMPLOYEE_INVALID)
    private int employeeMaxNum;

    private String url;

    @NotNull
    private String sizeId;
}
