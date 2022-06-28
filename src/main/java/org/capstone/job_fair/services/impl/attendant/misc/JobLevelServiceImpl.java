package org.capstone.job_fair.services.impl.attendant.misc;


import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.attendant.misc.JobLevelDTO;
import org.capstone.job_fair.models.entities.attendant.misc.JobLevelEntity;
import org.capstone.job_fair.repositories.attendant.misc.JobLevelRepository;
import org.capstone.job_fair.services.interfaces.attendant.misc.JobLevelService;
import org.capstone.job_fair.services.mappers.attendant.misc.JobLevelMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class JobLevelServiceImpl implements JobLevelService {

    @Autowired
    private JobLevelRepository jobLevelRepository;

    @Autowired
    private JobLevelMapper jobLevelMapper;

    @Override
    public Optional<JobLevelDTO> findById(int id) {
        return jobLevelRepository.findById(id).map(entity -> jobLevelMapper.toDTO(entity));
    }

    @Override
    public List<JobLevelDTO> getAll() {
        return jobLevelRepository.findAll().stream().map(entity -> jobLevelMapper.toDTO(entity)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public JobLevelDTO delete(int id) {
        Optional<JobLevelEntity> entityOptional = jobLevelRepository.findById(id);
        if (!entityOptional.isPresent()) throw new
                IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobLevel.NOT_FOUND));
        jobLevelRepository.delete(entityOptional.get());
        return jobLevelMapper.toDTO(entityOptional.get());
    }

    @Override
    @Transactional
    public JobLevelDTO create(JobLevelDTO dto) {
        JobLevelEntity entity = jobLevelRepository.save(jobLevelMapper.toEntity(dto));
        return jobLevelMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public JobLevelDTO update(JobLevelDTO dto) {
        Optional<JobLevelEntity> entityOptional = jobLevelRepository.findById(Integer.parseInt(dto.getId()));
        if (!entityOptional.isPresent()) throw new
                IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobLevel.NOT_FOUND));
        JobLevelEntity entity = jobLevelRepository.save(jobLevelMapper.toEntity(dto));
        return jobLevelMapper.toDTO(entity);
    }
}
