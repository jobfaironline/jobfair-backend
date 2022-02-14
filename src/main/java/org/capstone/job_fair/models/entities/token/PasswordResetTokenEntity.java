package org.capstone.job_fair.models.entities.token;

import lombok.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "password_reset_token", schema = "dbo")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PasswordResetTokenEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "otp")
    private String otp;

    @Column(name = "expired_time")
    private Long expiredTime;


    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "is_invalidated")
    private boolean isInvalidated;

    @OneToOne(targetEntity = AccountEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "account_id")
    private AccountEntity account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PasswordResetTokenEntity that = (PasswordResetTokenEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
