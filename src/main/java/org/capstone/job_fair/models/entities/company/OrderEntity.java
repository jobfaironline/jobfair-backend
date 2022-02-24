package org.capstone.job_fair.models.entities.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.capstone.job_fair.models.statuses.OrderStatus;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order", schema = "dbo")
public class OrderEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    @Column(name = "total", nullable = false)
    private Double total;
    @Column(name = "createDate", nullable = false)
    private Long createDate;
    @Column(name = "cancelDate")
    private Long cancelDate;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_registration_id")
    private CompanyRegistrationEntity companyRegistration;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrderEntity that = (OrderEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
