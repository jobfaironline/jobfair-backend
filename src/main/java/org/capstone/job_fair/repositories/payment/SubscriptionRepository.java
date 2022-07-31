package org.capstone.job_fair.repositories.payment;


import org.capstone.job_fair.models.entities.payment.SubscriptionEntity;
import org.capstone.job_fair.models.statuses.SubscriptionStatus;
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

    @Query("select s from SubscriptionEntity s " +
            "where s.company.id = ?1 and s.currentPeriodStart > ?2 and s.currentPeriodEnd < ?2 and s.status <> ?3 or s.status is null")
    Optional<SubscriptionEntity> findByCompanyIdAndCurrentPeriodStartAfterAndCurrentPeriodEndBeforeAndStatusNotOrStatusNull(String companyId, Long date, SubscriptionStatus status);

    Page<SubscriptionEntity> findAllByCompanyId(String companyId, Pageable pageable);

    Optional<SubscriptionEntity> findByCompanyIdAndId(String companyId, String subscriptionId);
}
