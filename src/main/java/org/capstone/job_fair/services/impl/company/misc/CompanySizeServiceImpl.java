package org.capstone.job_fair.services.impl.company.misc;

import org.capstone.job_fair.repositories.company.misc.CompanySizeRepository;
import org.capstone.job_fair.services.interfaces.company.misc.CompanySizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CompanySizeServiceImpl implements CompanySizeService {

    @Autowired
    private CompanySizeRepository sizeRepository;

    @Override
    public Integer getCountBySizeId(int id) {
        return sizeRepository.countById(id);
    }
}
