package org.capstone.job_fair.controllers.payload.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.constants.DataConstraint;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLayoutMetaDataRequest {
    @Size(min = DataConstraint.Layout.NAME_MIN_LENGTH, max = DataConstraint.Layout.NAME_MAX_LENGTH)
    private String name;
    @Size(min = DataConstraint.Layout.DESCRIPTION_MIN_LENGTH, max = DataConstraint.Layout.DESCRIPTION_MAX_LENGTH)
    private String description;
}
