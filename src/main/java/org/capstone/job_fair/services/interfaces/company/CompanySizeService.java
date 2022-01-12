package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.entities.company.CompanySizeEntity;

import java.util.Optional;

public interface CompanySizeService {
    Optional<CompanySizeEntity> findBySizeId(String id);
}
