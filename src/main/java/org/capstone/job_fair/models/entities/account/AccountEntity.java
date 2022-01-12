package org.capstone.job_fair.models.entities.account;

import lombok.*;
import org.capstone.job_fair.models.statuses.AccountStatus;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account", schema = "dbo")
public class AccountEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    @Basic
    @Column(name = "email", nullable = false, length = 322)
    private String email;
    @Basic
    @Column(name = "password", nullable = false, length = 50)
    private String password;
    @Basic
    @Column(name = "status", nullable = true)
    @Enumerated(EnumType.ORDINAL)
    private AccountStatus status;
    @Basic
    @Column(name = "phone", nullable = true, length = 11)
    private String phone;

    @Basic
    @Column(name = "profile_image_url", nullable = true, length = 2048)
    private String profileImageUrl;

    @Basic
    @Column(name = "firstname", nullable = true, length = 100)
    private String firstname;

    @Basic
    @Column(name = "lastname", nullable = true, length = 100)
    private String lastname;

    @Basic
    @Column(name = "middlename", nullable = true, length = 100)
    private String middlename;

    @ManyToOne()
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @ManyToOne()
    @JoinColumn(name = "gender_id")
    private GenderEntity gender;
    public String getFullname(){
        return firstname + " " + middlename + " " + lastname;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountEntity that = (AccountEntity) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
