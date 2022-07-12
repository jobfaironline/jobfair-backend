package org.capstone.job_fair.controllers.payload.requests.job_fair;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.controllers.payload.requests.company.BoothDescriptionRequest;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoothJobPositionRequest {
    @Valid
    private List<BoothDescriptionRequest.JobPosition> jobPositions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class JobPosition {
        @NotNull
        private String id;
        @NotNull
        @NumberFormat
        @Min(DataConstraint.JobPosition.SALARY_MIN)
        @Max(DataConstraint.JobPosition.SALARY_MAX)
        private Double minSalary;
        @NumberFormat
        @Min(DataConstraint.JobPosition.SALARY_MIN)
        @Max(DataConstraint.JobPosition.SALARY_MAX)
        private Double maxSalary;
        @NotNull
        @NumberFormat
        @Min(DataConstraint.JobPosition.EMPLOYEE_MIN)
        @Max(DataConstraint.JobPosition.EMPLOYEE_MAX)
        private Integer numOfPosition;

        @NotNull
        private Boolean isHaveTest;
        @NumberFormat
        @Min(DataConstraint.JobPosition.TEST_LENGTH_MIN)
        @Max(DataConstraint.JobPosition.TEST_LENGTH_MAX)
        private Integer testLength;
        @NumberFormat
        @Min(DataConstraint.JobPosition.NUM_OF_QUESTION_MIN)
        @Max(DataConstraint.JobPosition.NUM_OF_QUESTION_MAX)
        private Integer testNumOfQuestion;
        @NumberFormat
        @Min(DataConstraint.JobPosition.PASS_MARK_MIN)
        @Max(DataConstraint.JobPosition.PASS_MARK_MAX)
        private Double passMark;
        @Length(max = DataConstraint.JobPosition.NOTE_LENGTH)
        private String note;

    }
}
