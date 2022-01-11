package org.capstone.job_fair.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "password_reset_token", schema = "dbo")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordResetTokenEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "otp")
    private String otp;

    @Column(name = "expired_time")
    private float expiredTime;


    @Column(name = "create_time")
    private float createTime;

    @Column(name = "is_invalidated")
    private boolean isInvalidated;

    @OneToOne(targetEntity = AccountEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "account_id")
    private AccountEntity account;

}
