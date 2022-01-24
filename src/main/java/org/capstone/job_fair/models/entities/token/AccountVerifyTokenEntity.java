package org.capstone.job_fair.models.entities.token;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "account_verify_token", schema = "dbo", catalog = "")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountVerifyTokenEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;
    @Basic
    @Column(name = "account_id")
    private String accountId;
    @Basic
    @Column(name = "is_invalidated")
    private Boolean isInvalidated;
    @Basic
    @Column(name = "created_time")
    private Long createdTime;
    @Basic
    @Column(name = "expired_time")
    private Long expiredTime;


}
