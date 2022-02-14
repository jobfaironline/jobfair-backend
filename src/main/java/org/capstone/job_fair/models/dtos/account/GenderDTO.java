package org.capstone.job_fair.models.dtos.account;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GenderDTO {
    @EqualsAndHashCode.Include
    private int id;
    private String name;
}
