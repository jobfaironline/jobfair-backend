package org.capstone.job_fair.services.impl.attendant.misc;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.attendant.misc.ResidenceDTO;
import org.capstone.job_fair.models.entities.attendant.misc.ResidenceEntity;
import org.capstone.job_fair.repositories.attendant.misc.ResidenceRepository;
import org.capstone.job_fair.services.interfaces.attendant.misc.ResidenceService;
import org.capstone.job_fair.services.mappers.attendant.misc.ResidenceMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ResidenceServiceImpl implements ResidenceService {


    @Autowired
    private ResidenceRepository residenceRepository;

    @Autowired
    private ResidenceMapper residenceMapper;

    @Override
    public Integer getCountResidenceById(int id) {
        return residenceRepository.countById(id);
    }

    @Override
    public Optional<ResidenceDTO> findById(int id) {
        return residenceRepository.findById(id).map(entity -> residenceMapper.toDTO(entity));
    }

    @Override
    public List<ResidenceDTO> getAll() {
        return residenceRepository.findAll().stream().map(entity -> residenceMapper.toDTO(entity)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResidenceDTO delete(int id) {
        Optional<ResidenceEntity> entityOptional = residenceRepository.findById(id);
        if(!entityOptional.isPresent()) throw new
                IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Residence.NOT_FOUND));
        residenceRepository.deleteById(id);
        return residenceMapper.toDTO(entityOptional.get());
    }

    @Override
    @Transactional
    public ResidenceDTO create(ResidenceDTO dto) {
        ResidenceEntity entity = residenceRepository.save(residenceMapper.toEntity(dto));
        return residenceMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public ResidenceDTO update(ResidenceDTO dto) {
        Optional<ResidenceEntity> entityOptional = residenceRepository.findById(dto.getId());
        if(!entityOptional.isPresent()) throw new
                IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Residence.NOT_FOUND));
        ResidenceEntity entity = residenceRepository.save(residenceMapper.toEntity(dto));
        return residenceMapper.toDTO(entity);
    }
}
