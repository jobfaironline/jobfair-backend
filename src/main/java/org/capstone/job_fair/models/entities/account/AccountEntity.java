package org.capstone.job_fair.models.entities.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account", schema = "dbo")
public class AccountEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "email", nullable = false, length = 322)
    private String email;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AccountStatus status;

    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "profile_image_url", nullable = false, length = 2048)
    private String profileImageUrl;

    @Column(name = "firstname", nullable = false, length = 100)
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 100)
    private String lastname;

    @Column(name = "middlename", length = 100)
    private String middlename;

    @Column(name = "create_time")
    private Long createTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_id", nullable = false)
    private GenderEntity gender;

    public String getFullname() {
        return firstname + " " + middlename + " " + lastname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AccountEntity account = (AccountEntity) o;
        return id != null && Objects.equals(id, account.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "email = " + email + ", " +
                "password = " + password + ", " +
                "status = " + status + ", " +
                "phone = " + phone + ", " +
                "profileImageUrl = " + profileImageUrl + ", " +
                "firstname = " + firstname + ", " +
                "lastname = " + lastname + ", " +
                "middlename = " + middlename + ")";
    }
}
