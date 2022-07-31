package org.capstone.job_fair.repositories.payment;


import org.capstone.job_fair.models.entities.payment.SubscriptionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, String> {
    @Query("SELECT s FROM SubscriptionEntity s WHERE s.company.id = ?1 and s.currentPeriodStart < ?2 and s.currentPeriodEnd > ?2")
    Optional<SubscriptionEntity> findCurrentSubscriptionByCompanyId(String companyId, Long date);

    Page<SubscriptionEntity> findAllByCompanyId(String companyId, Pageable pageable);

    Optional<SubscriptionEntity> findByCompanyIdAndId(String companyId, String subscriptionId);
}
