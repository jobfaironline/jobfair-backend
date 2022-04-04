package org.capstone.job_fair.models.dtos.company;

import lombok.*;
import org.capstone.job_fair.models.dtos.job_fair.BoothDTO;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CompanyBoothDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private Double price;
    private OrderDTO order;
    private BoothDTO booth;
}
