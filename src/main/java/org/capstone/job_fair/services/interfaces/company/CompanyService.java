package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.entities.company.CompanyEntity;

import java.util.Optional;

public interface CompanyService {
    Optional<CompanyEntity> findCompanyById(String id);
}
