package org.capstone.job_fair.services.impl.company.misc;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.company.misc.SkillTagDTO;
import org.capstone.job_fair.models.entities.company.misc.SkillTagEntity;
import org.capstone.job_fair.repositories.company.misc.SkillTagRepository;
import org.capstone.job_fair.services.interfaces.company.misc.SkillService;
import org.capstone.job_fair.services.mappers.company.misc.SkillTagMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillTagRepository skillTagRepository;

    @Autowired
    private SkillTagMapper skillTagMapper;


    @Override
    public Optional<SkillTagDTO> findById(int id) {
        return skillTagRepository.findById(id).map(skillTagEntity -> skillTagMapper.toDTO(skillTagEntity));
    }

    @Override
    public List<SkillTagDTO> getAll() {
        return skillTagRepository.findAll().stream().map(skillTagEntity -> skillTagMapper.toDTO(skillTagEntity)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SkillTagDTO delete(int id) {
        Optional<SkillTagEntity> entityOptional = skillTagRepository.findById(id);
        if(!entityOptional.isPresent()) throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.SkillTag.NOT_FOUND));
        skillTagRepository.deleteById(id);
        return skillTagMapper.toDTO(entityOptional.get());
    }

    @Override
    @Transactional
    public SkillTagDTO create(SkillTagDTO dto) {
        SkillTagEntity entity = skillTagRepository.save(skillTagMapper.toEntity(dto));
        return skillTagMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public SkillTagDTO update(SkillTagDTO dto) {
        Optional<SkillTagEntity> entityOptional = skillTagRepository.findById(dto.getId());
        if(!entityOptional.isPresent()) throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.SkillTag.NOT_FOUND));
        SkillTagEntity entity = skillTagRepository.save(skillTagMapper.toEntity(dto));
        return skillTagMapper.toDTO(entity);
    }
}
