package org.capstone.job_fair.controllers.payload.requests.job_fair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.constants.DataConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDecoratedItemMetaDataRequest {
    @NotNull
    private String id;
    @Size(min = DataConstraint.DecoratedItem.NAME_MIN_LENGTH, max = DataConstraint.DecoratedItem.NAME_MAX_LENGTH)
    private String name;
    @Size(min = DataConstraint.DecoratedItem.DESCRIPTION_MIN_LENGTH, max = DataConstraint.DecoratedItem.DESCRIPTION_MAX_LENGTH)
    private String description;
}
