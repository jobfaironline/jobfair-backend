package org.capstone.job_fair.models.entities.token;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "account_verify_token", schema = "dbo")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        AccountVerifyTokenEntity that = (AccountVerifyTokenEntity) o;

        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
