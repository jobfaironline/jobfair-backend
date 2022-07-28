package org.capstone.job_fair.models.dtos.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminStatisticsDTO {

    private GeneralStatistics generalStatistics = new GeneralStatistics();
    private JobFairStatistics jobFairStatistics = new JobFairStatistics();
    private AccountStatistics accountStatistics = new AccountStatistics();

    @Data
    public static class GeneralStatistics {
        private long companyNum = 0;
        private long userNum = 0;
        private long visitorNum = 0;
    }

    @Data
    public static class JobFairStatistics {
        private long pastNum = 0;
        private long inProgressNum = 0;
        private long incomingNum = 0;
    }

    @Data
    public static class AccountStatistics {
        private long verifiedNum = 0;
        private long inactiveNum = 0;
        private long registeredNum = 0;
        private long suspendedNum = 0;
        private long attendantNum = 0;
        private long companyManagerNum = 0;
        private long companyEmployeeNum = 0;
    }
}
