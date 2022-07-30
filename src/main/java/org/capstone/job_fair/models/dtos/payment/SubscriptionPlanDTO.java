package org.capstone.job_fair.models.dtos.payment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.entities.payment.SubscriptionEntity;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionPlanDTO {
    private String id;
    private String name;
    private String description;
    private Double price;
    private Set<SubscriptionEntity> subscriptionEntities = new LinkedHashSet<>();

}
