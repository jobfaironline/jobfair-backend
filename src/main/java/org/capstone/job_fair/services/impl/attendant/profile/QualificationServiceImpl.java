package org.capstone.job_fair.services.impl.attendant.profile;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.attendant.profile.QualificationDTO;
import org.capstone.job_fair.models.entities.attendant.profile.QualificationEntity;
import org.capstone.job_fair.repositories.attendant.profile.QualificationRepository;
import org.capstone.job_fair.services.interfaces.attendant.profile.QualificationService;
import org.capstone.job_fair.services.mappers.attendant.profile.QualificationMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class QualificationServiceImpl implements QualificationService {

    @Autowired
    private QualificationRepository qualificationRepository;

    @Autowired
    private QualificationMapper qualificationMapper;

    @Override
    public Optional<QualificationDTO> findById(int id) {
        return qualificationRepository.findById(id).map(entity -> qualificationMapper.toDTO(entity));
    }

    @Override
    public List<QualificationDTO> getAll() {
        return qualificationRepository.findAll().stream().map(entity -> qualificationMapper.toDTO(entity)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QualificationDTO delete(int id) {
        Optional<QualificationEntity> entityOptional = qualificationRepository.findById(id);
        if(!entityOptional.isPresent()) throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Qualification.NOT_FOUND));
        qualificationRepository.delete(entityOptional.get());
        return qualificationMapper.toDTO(entityOptional.get());
     }

    @Override
    @Transactional
    public QualificationDTO create(QualificationDTO dto) {
        Optional<QualificationEntity> entityOptional = qualificationRepository.findById(Integer.parseInt(dto.getId()));
        if(entityOptional.isPresent()) throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Qualification.DUPLICATED));
        QualificationEntity entity = qualificationMapper.toEntity(dto);
        qualificationRepository.save(entity);
        return qualificationMapper.toDTO(entity);

    }

    @Override
    @Transactional
    public QualificationDTO update(QualificationDTO dto) {
        Optional<QualificationEntity> entityOptional = qualificationRepository.findById(Integer.parseInt(dto.getId()));
        if(!entityOptional.isPresent()) throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Qualification.NOT_FOUND));
        QualificationEntity entity = qualificationRepository.save(qualificationMapper.toEntity(dto));
        return qualificationMapper.toDTO(entity);
    }
}
