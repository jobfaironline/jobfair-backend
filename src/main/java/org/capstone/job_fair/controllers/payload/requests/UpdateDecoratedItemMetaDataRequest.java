package org.capstone.job_fair.controllers.payload.requests;

import org.capstone.job_fair.constants.DataConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateDecoratedItemMetaDataRequest {
    @NotNull
    private String id;
    @Size(min = DataConstraint.DecoratedItem.NAME_MIN_LENGTH, max = DataConstraint.DecoratedItem.NAME_MAX_LENGTH)
    private String name;
    @Size(min = DataConstraint.DecoratedItem.DESCRIPTION_MIN_LENGTH, max = DataConstraint.DecoratedItem.DESCRIPTION_MAX_LENGTH)
    private String description;
}
