package org.capstone.job_fair.models.dtos.account;

import lombok.*;
import org.capstone.job_fair.models.enums.Gender;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.models.statuses.AccountStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AccountDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private String email;
    private String password;
    private AccountStatus status;
    private String phone;
    private String profileImageUrl;
    private String firstname;
    private String lastname;
    private String middlename;
    private Gender gender;
    private Role role;
}
