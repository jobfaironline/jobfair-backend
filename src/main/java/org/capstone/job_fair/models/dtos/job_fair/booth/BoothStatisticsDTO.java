package org.capstone.job_fair.models.dtos.job_fair.booth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoothStatisticsDTO {
    private Booth booth = new Booth();
    private CvStatistics cvStatistics = new CvStatistics();
    private List<JobPosition> jobPositions = new ArrayList<>();

    @Data
    public static class Booth {
        private String id;
        private String name;
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
        private double matchingPointAverage = 0;
        private int approveCV = 0;
    }
}
