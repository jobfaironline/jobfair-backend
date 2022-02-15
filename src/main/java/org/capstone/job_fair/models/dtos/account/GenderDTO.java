package org.capstone.job_fair.models.dtos.account;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GenderDTO {
    @EqualsAndHashCode.Include
    private int id;
    private String name;
}
