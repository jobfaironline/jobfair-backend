package org.capstone.job_fair.models.dtos.account;

import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;


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
    private String description;
}
