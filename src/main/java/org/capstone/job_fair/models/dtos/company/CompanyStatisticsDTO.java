package org.capstone.job_fair.models.dtos.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.capstone.job_fair.models.dtos.job_fair.JobFairStatisticsDTO;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyStatisticsDTO {

    private GeneralStatistics generalStatistics = new GeneralStatistics();
    private CvStatistics cvStatistics = new CvStatistics();
    private List<CompanyStatisticsDTO.JobPosition> jobPositions = new ArrayList<>();
    private List<JobFair> jobFairs = new ArrayList<>();

    @Data
    public static class GeneralStatistics {
        private int createdJobFairNum = 0;
        private int publishedJobFairNum = 0;
        private int participantNum = 0;
        private int boothTotalNums = 0;
        private int inProgressJobFairNum = 0;
        private int pastJobFairNum = 0;
        private int upComingJobFairNum = 0;
        private String companyName = "";
    }

    @Data
    public static class CvStatistics {
        private int cvNum = 0;
        private int approvedCvNum = 0;
        private int rejectedCvNum = 0;
        private int pendingCvNum = 0;
        private int interviewNum = 0;
        private int doneInterviewNum = 0;
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
    public static class JobFair {
        private String id;
        private String name = "";
        private int boothNum = 0;
        private int participationNum = 0;
        private int jobPositionNum = 0;
        private int employeeNum = 0;
        private long decorateStartTime = 0;
        private long publicEndTime = 0;
    }


}
