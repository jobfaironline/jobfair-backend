package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.models.entities.company.CompanySizeEntity;
import org.capstone.job_fair.repositories.company.CompanySizeRepository;
import org.capstone.job_fair.services.interfaces.company.CompanySizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanySizeServiceImpl implements CompanySizeService {

    @Autowired
    private CompanySizeRepository sizeRepository;

    @Override
    public Integer getCountBySizeId(int id) {
        return sizeRepository.countById(id);
    }
}
