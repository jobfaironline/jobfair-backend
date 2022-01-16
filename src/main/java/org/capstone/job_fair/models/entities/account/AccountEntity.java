package org.capstone.job_fair.models.entities.account;

import lombok.*;
import org.capstone.job_fair.models.statuses.AccountStatus;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "account", schema = "dbo")
public class AccountEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "email", nullable = false, length = 322)
    private String email;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AccountStatus status;

    @Column(name = "phone", nullable = true, length = 11)
    private String phone;

    @Column(name = "profile_image_url", nullable = false, length = 2048)
    private String profileImageUrl;

    @Column(name = "firstname", nullable = false, length = 100)
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 100)
    private String lastname;

    @Column(name = "middlename", nullable = true, length = 100)
    private String middlename;

    @ManyToOne()
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @ManyToOne()
    @JoinColumn(name = "gender_id", nullable = false)
    private GenderEntity gender;

    public String getFullname() {
        return firstname + " " + middlename + " " + lastname;
    }

}
