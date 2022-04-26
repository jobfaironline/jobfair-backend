package org.capstone.job_fair.services.impl.attendant;


import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.attendant.JobLevelDTO;
import org.capstone.job_fair.models.entities.attendant.JobLevelEntity;
import org.capstone.job_fair.repositories.attendant.JobLevelRepository;
import org.capstone.job_fair.services.interfaces.attendant.JobLevelService;
import org.capstone.job_fair.services.mappers.attendant.JobLevelMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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
    public JobLevelDTO delete(int id) {
        Optional<JobLevelEntity> entityOptional = jobLevelRepository.findById(id);
        if(!entityOptional.isPresent()) throw new
                IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobLevel.NOT_FOUND));
        jobLevelRepository.delete(entityOptional.get());
        return jobLevelMapper.toDTO(entityOptional.get());
    }

    @Override
    public JobLevelDTO create(JobLevelDTO dto) {
        Optional<JobLevelEntity> entityOptional = jobLevelRepository.findById(Integer.parseInt(dto.getId()));
        if(entityOptional.isPresent()) throw new
                IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobLevel.DUPLICATED));
        JobLevelEntity entity = jobLevelRepository.save(jobLevelMapper.toEntity(dto));
        return jobLevelMapper.toDTO(entity);
    }

    @Override
    public JobLevelDTO update(JobLevelDTO dto) {
        Optional<JobLevelEntity> entityOptional = jobLevelRepository.findById(Integer.parseInt(dto.getId()));
        if(!entityOptional.isPresent()) throw new
                IllegalArgumentException(MessageUtil.getMessage(MessageConstant.JobLevel.NOT_FOUND));
        JobLevelEntity entity = jobLevelRepository.save(jobLevelMapper.toEntity(dto));
        return jobLevelMapper.toDTO(entity);
    }
}