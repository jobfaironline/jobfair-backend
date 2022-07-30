package org.capstone.job_fair.repositories.payment;

import org.capstone.job_fair.models.entities.payment.SubscriptionPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlanEntity, String> {
}
