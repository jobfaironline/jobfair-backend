package org.capstone.job_fair.controllers.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.validators.XSSConstraint;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDecoratedItemMetaDataRequest {
    @NotBlank
    @XSSConstraint
    @Size(min = DataConstraint.DecoratedItem.NAME_MIN_LENGTH, max = DataConstraint.DecoratedItem.NAME_MAX_LENGTH)
    private String name;
    @NotBlank
    @XSSConstraint
    @Size(min = DataConstraint.DecoratedItem.DESCRIPTION_MIN_LENGTH, max = DataConstraint.DecoratedItem.DESCRIPTION_MAX_LENGTH)
    private String description;

    @NotNull
    @NumberFormat
    @Min(DataConstraint.DecoratedItem.SIZE_MIN)
    @Max(DataConstraint.DecoratedItem.SIZE_MAX)
    private int size;
}
