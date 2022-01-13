package org.capstone.job_fair.models.dtos.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.models.enums.Gender;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class AccountDTO implements Serializable {
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
    public AccountDTO() {

    }
}
