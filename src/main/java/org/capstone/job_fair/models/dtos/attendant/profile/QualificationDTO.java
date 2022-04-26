package org.capstone.job_fair.models.dtos.attendant.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QualificationDTO {
    @NotNull
    @NumberFormat
    private String id;
    @NotBlank
    private String name;
}
