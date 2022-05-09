package org.capstone.job_fair.models.dtos.company.misc;

import lombok.*;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProfessionCategoryDTO implements Serializable {
    @EqualsAndHashCode.Include
    @NotNull
    @NumberFormat
    private Integer id;
    @NotBlank
    private String name;
}
