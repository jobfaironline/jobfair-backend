package org.capstone.job_fair.models.entities.payment;

import lombok.*;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.job_fair.ShiftEntity;
import org.capstone.job_fair.models.statuses.SubscriptionRefundStatus;
import org.capstone.job_fair.models.statuses.SubscriptionStatus;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "subscription", schema = "dbo")
public class SubscriptionEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "status")
    private SubscriptionStatus status;

    @Column(name = "current_period_start")
    private Long currentPeriodStart;

    @Column(name = "current_period_end")
    private Long currentPeriodEnd;

    @Column(name = "cancel_at")
    private Long cancelAt;

    @Column(name = "default_payment_method")
    private Integer defaultPaymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_plan_id")
    private SubscriptionPlanEntity subscriptionPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    @Column(name = "price")
    private Double price;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "refund_status")
    private SubscriptionRefundStatus refundStatus;

    @Column(name = "refund_reason")
    private String refundReason;

    @Column(name = "jobfair_quota")
    private Integer jobfairQuota;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ShiftEntity that = (ShiftEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}