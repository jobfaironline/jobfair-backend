package org.capstone.job_fair.controllers.payload.requests.job_fair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.validators.XSSConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLayoutMetaDataRequest {
    @XSSConstraint
    @NotNull
    @Size(min = DataConstraint.Layout.NAME_MIN_LENGTH, max = DataConstraint.Layout.NAME_MAX_LENGTH)
    private String name;
    @XSSConstraint
    @NotNull
    @Size(min = DataConstraint.Layout.DESCRIPTION_MIN_LENGTH, max = DataConstraint.Layout.DESCRIPTION_MAX_LENGTH)
    private String description;
}
