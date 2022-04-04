package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.JobFairBoothDTO;

public interface CompanyBoothPurchaseService {
    JobFairBoothDTO purchaseBooth(String companyRegistrationId, String boothId);
}
