package org.capstone.job_fair.models.dtos.company;

import lombok.*;
import org.capstone.job_fair.models.statuses.OrderStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private Double total;
    private Long createDate;
    private Long cancelDate;
    private OrderStatus status;
    private CompanyRegistrationDTO companyRegistration;
}
