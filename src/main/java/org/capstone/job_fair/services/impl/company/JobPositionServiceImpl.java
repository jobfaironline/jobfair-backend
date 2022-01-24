package org.capstone.job_fair.services.impl.company;

import lombok.AllArgsConstructor;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.company.job.JobPositionEntity;
import org.capstone.job_fair.repositories.company.job.JobPositionRepository;
import org.capstone.job_fair.services.interfaces.company.JobPositionService;
import org.capstone.job_fair.services.mappers.JobPositionEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class JobPositionServiceImpl implements JobPositionService {
    @Autowired
    private JobPositionRepository jobPositionRepository;
    @Autowired
    private JobPositionEntityMapper mapper;

    @Override
    public void createNewJobPosition(JobPositionDTO dto) {
        JobPositionEntity entity = mapper.toEntity(dto);

        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setId(dto.getCompanyDTO().getId());


        entity.setCompany(companyEntity);

        jobPositionRepository.save(entity);
    }
}
