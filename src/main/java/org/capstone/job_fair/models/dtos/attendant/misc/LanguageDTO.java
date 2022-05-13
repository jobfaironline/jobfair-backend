package org.capstone.job_fair.models.dtos.attendant.misc;

import lombok.*;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LanguageDTO implements Serializable {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
}
