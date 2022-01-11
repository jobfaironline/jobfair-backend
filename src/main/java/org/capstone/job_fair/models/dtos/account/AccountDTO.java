package org.capstone.job_fair.models.dtos.account;

import lombok.Data;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.models.enums.Gender;

import java.io.Serializable;

@Data
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
