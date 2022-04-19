package org.capstone.job_fair.controllers.payload.requests.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.constants.DataConstraint;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoothDescriptionRequest {

    @NotNull
    private String boothId;
    @NotNull
    @Length(max= DataConstraint.JobPosition.DESCRIPTION_LENGTH)
    private String description;
    @Valid
    private List<JobPosition> jobPositions;

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
        @NotNull
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
        @Length(max = DataConstraint.JobPosition.NOTE_LENGTH)
        private String note;
        @NotNull
        @NumberFormat
        @Min(DataConstraint.JobPosition.TEST_LENGTH_MIN)
        @Max(DataConstraint.JobPosition.TEST_LENGTH_MAX)
        private Integer testLength;
        @NotNull
        @NumberFormat
        @Min(DataConstraint.JobPosition.NUM_OF_QUESTION_MIN)
        @Max(DataConstraint.JobPosition.NUM_OF_QUESTION_MAX)
        private Integer testNumOfQuestion;
        @NotNull
        @NumberFormat
        @Min(DataConstraint.JobPosition.PASS_MARK_MIN)
        @Max(DataConstraint.JobPosition.PASS_MARK_MAX)
        private Double passMark;

    }
}
