package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.CompanyBoothDTO;

public interface CompanyBoothPurchaseService {
    CompanyBoothDTO purchaseBooth(String companyRegistrationId, String boothId);
}
