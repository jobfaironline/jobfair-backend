package org.capstone.job_fair.models.entities.token;

import lombok.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.springframework.data.domain.Persistable;

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

}
