package org.capstone.job_fair.services.impl.company;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.company.ProfessionCategoryDTO;
import org.capstone.job_fair.models.entities.company.ProfessionCategoryEntity;
import org.capstone.job_fair.repositories.company.ProfessionalCategoryRepository;
import org.capstone.job_fair.services.interfaces.company.ProfessionalCategoryService;
import org.capstone.job_fair.services.mappers.company.ProfessionCategoryEntityMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProfessionalCategoryServiceImpl implements ProfessionalCategoryService {

    @Autowired
    private ProfessionalCategoryRepository professionalCategoryRepository;

    @Autowired
    private ProfessionCategoryEntityMapper professionCategoryEntityMapper;

    @Override
    public Optional<ProfessionCategoryDTO> findById(int id) {
        return professionalCategoryRepository.findById(id).map(professionCategoryEntity -> professionCategoryEntityMapper.toDTO(professionCategoryEntity));
    }

    @Override
    public List<ProfessionCategoryDTO> getAll() {
        return professionalCategoryRepository.findAll().stream().map(professionCategoryEntity -> professionCategoryEntityMapper.toDTO(professionCategoryEntity)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProfessionCategoryDTO delete(int id) {
        Optional<ProfessionCategoryEntity> professionCategoryEntityOptional = professionalCategoryRepository.findById(id);
        if (!professionCategoryEntityOptional.isPresent()) throw new
                IllegalArgumentException(MessageUtil.getMessage(MessageConstant.ProfessionalCategory.NOT_FOUND));
        professionalCategoryRepository.delete(professionCategoryEntityOptional.get());
        return professionCategoryEntityMapper.toDTO(professionCategoryEntityOptional.get());
    }

    @Override
    @Transactional
    public ProfessionCategoryDTO create(ProfessionCategoryDTO dto) {
        Optional<ProfessionCategoryEntity> professionCategoryEntityOptional = professionalCategoryRepository.findById(dto.getId());
        if (professionCategoryEntityOptional.isPresent()) throw new
                IllegalArgumentException(MessageUtil.getMessage(MessageConstant.ProfessionalCategory.DUPLICATED));
        ProfessionCategoryEntity entity = professionalCategoryRepository.save(professionCategoryEntityMapper.toEntity(dto));
        return professionCategoryEntityMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public ProfessionCategoryDTO update(ProfessionCategoryDTO dto) {
        Optional<ProfessionCategoryEntity> professionCategoryEntityOptional = professionalCategoryRepository.findById(dto.getId());
        if (!professionCategoryEntityOptional.isPresent()) throw new
                IllegalArgumentException(MessageUtil.getMessage(MessageConstant.ProfessionalCategory.NOT_FOUND));
        ProfessionCategoryEntity entity = professionalCategoryRepository.save(professionCategoryEntityMapper.toEntity(dto));
        return professionCategoryEntityMapper.toDTO(entity);
    }
}
