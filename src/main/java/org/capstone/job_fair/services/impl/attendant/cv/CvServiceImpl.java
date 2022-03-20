package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.CvDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.capstone.job_fair.repositories.attendant.cv.CvRepository;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.mappers.attendant.cv.CvMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CvServiceImpl implements CvService {
    @Autowired
    private CvRepository cvRepository;

    @Autowired
    private CvMapper cvMapper;

    @Override
    @Transactional
    public CvDTO draftCv(CvDTO dto) {

        CvEntity entity = cvMapper.toEntity(dto);
        entity.getActivities().forEach(activity -> activity.setCv(entity));
        entity.getCertifications().forEach(certification -> certification.setCv(entity));
        entity.getEducations().forEach(education -> education.setCv(entity));
        entity.getReferences().forEach(reference -> reference.setCv(entity));
        entity.getWorkHistories().forEach(history -> history.setCv(entity));
        entity.getSkills().forEach(skill -> skill.setCv(entity));

        CvEntity newEntity = cvRepository.save(cvMapper.toEntity(dto));
        return cvMapper.toDTO(newEntity);
    }

    @Override
    public List<CvDTO> getAllByAttendantId(String attendantId) {
        return cvRepository.findByAttendantAccountId(attendantId)
                .stream()
                .map(cvMapper::toDTO)
                .collect(Collectors.toList());
    }
}
