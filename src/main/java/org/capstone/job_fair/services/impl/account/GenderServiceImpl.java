package org.capstone.job_fair.services.impl.account;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.account.GenderDTO;
import org.capstone.job_fair.models.entities.account.GenderEntity;
import org.capstone.job_fair.repositories.account.GenderRepository;
import org.capstone.job_fair.services.interfaces.account.GenderService;
import org.capstone.job_fair.services.mappers.account.GenderMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class GenderServiceImpl implements GenderService {

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private GenderMapper mapper;

    @Override
    public Optional<GenderDTO> findById(int id) {
        return genderRepository.findById(id).map(entity -> mapper.toDTO(entity));
    }

    @Override
    public List<GenderDTO> getAll() {
        return genderRepository.findAll().stream().map(entity -> mapper.toDTO(entity)).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public GenderDTO createGender(GenderDTO dto) {
        Optional<GenderEntity> entityOptional = genderRepository.findById(dto.getId());
        if(entityOptional.isPresent()) throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Gender.DUPLICATED_GENDER));
        GenderEntity entity = mapper.toEntity(dto);
        entity = genderRepository.save(entity);
        return mapper.toDTO(entity);
    }
    @Override
    @Transactional
    public GenderDTO deleteGender(int id) {
        Optional<GenderEntity> entityOptional = genderRepository.findById(id);
        if (!entityOptional.isPresent())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Gender.NOT_FOUND));
        genderRepository.delete(entityOptional.get());
        return mapper.toDTO(entityOptional.get());
    }
    @Override
    @Transactional
    public GenderDTO updateGender(GenderDTO dto) {
        Optional<GenderEntity> entityOptional = genderRepository.findById(dto.getId());
        if (!entityOptional.isPresent())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Gender.NOT_FOUND));
        genderRepository.save(mapper.toEntity(dto));
        return dto;
    }
}
