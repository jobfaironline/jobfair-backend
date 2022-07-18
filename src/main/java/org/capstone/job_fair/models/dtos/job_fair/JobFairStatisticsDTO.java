package org.capstone.job_fair.models.dtos.job_fair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobFairStatisticsDTO {
    private JobFair jobFair = new JobFairStatisticsDTO.JobFair();
    private GeneralStatistics generalStatistics = new GeneralStatistics();
    private CvStatistics cvStatistics = new CvStatistics();
    private List<JobPosition> jobPositions = new ArrayList<>();
    private List<Booth> booths = new ArrayList<>();


    @Data
    public static class JobFair {
        private String id;
        private String name;
    }

    @Data
    public static class GeneralStatistics {
        private int boothNum = 0;
        private int participationNum = 0;
        private int jobPositionNum = 0;
        private int employeeNum = 0;
    }

    @Data
    public static class CvStatistics {
        private long pendingNum = 0;
        private long approvedNum = 0;
        private long rejectNum = 0;
        private double matchingAverage = 0.0;
        private double[] matchingRange = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    @Data
    public static class JobPosition {
        private String id;
        private String name;
        private int goal = 0;
        private int current = 0;
        private int approveCV = 0;
        private double matchingPointAverage = 0;
    }

    @Data
    public static class Booth {
        private String id;
        private String name;
        private int visitNum = 0;
        private int cvNum = 0;
        private int approveCV = 0;
        private double matchingPointAverage = 0.0;
    }
}
