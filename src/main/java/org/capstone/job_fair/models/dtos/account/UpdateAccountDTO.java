package org.capstone.job_fair.models.dtos.account;

import lombok.*;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.validators.EmailConstraint;
import org.capstone.job_fair.validators.PasswordConstraint;
import org.capstone.job_fair.validators.PhoneConstraint;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateAccountDTO {
    @EmailConstraint
    private String email;
    @PasswordConstraint
    private String password;
    @PhoneConstraint
    private String phone;
    @NotBlank(message = "profile image" + MessageConstant.InvalidFormat.NOT_BLANK_FORMAT)
    private String profileImageURL;
    @NotBlank(message = "first name" + MessageConstant.InvalidFormat.NOT_BLANK_FORMAT)
    private String firstName;
    @NotBlank(message = "last name" + MessageConstant.InvalidFormat.NOT_BLANK_FORMAT)
    private String lastName;
    private String middleName;
    private String genderID;
}
