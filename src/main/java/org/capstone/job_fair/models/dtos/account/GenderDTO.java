package org.capstone.job_fair.models.dtos.account;

import lombok.*;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GenderDTO {
    @EqualsAndHashCode.Include
    @NotNull
    @NumberFormat
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;

}
