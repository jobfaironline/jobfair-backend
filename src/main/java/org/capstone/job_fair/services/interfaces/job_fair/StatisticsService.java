package org.capstone.job_fair.services.interfaces.job_fair;

import org.capstone.job_fair.models.dtos.company.CompanyStatisticsDTO;
import org.capstone.job_fair.models.dtos.job_fair.JobFairStatisticsDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.BoothStatisticsDTO;

public interface StatisticsService {
    JobFairStatisticsDTO getJobFairStatistics(String jobFairId, String companyId);

    BoothStatisticsDTO getJobFairBoothStatistics(String boothId, String companyId);

    CompanyStatisticsDTO getCompanyStatistics(String companyId);
}
