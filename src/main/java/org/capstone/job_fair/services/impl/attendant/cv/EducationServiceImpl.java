package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.EducationDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.capstone.job_fair.models.entities.attendant.cv.EducationEntity;
import org.capstone.job_fair.models.entities.attendant.cv.QualificationEntity;
import org.capstone.job_fair.repositories.attendant.cv.EducationRepository;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.interfaces.attendant.cv.EducationService;
import org.capstone.job_fair.services.interfaces.attendant.cv.QualificationService;
import org.capstone.job_fair.services.mappers.EducationEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class EducationServiceImpl implements EducationService {

    private static final String NOT_FOUND_QUALIFICATION = "Not found qualification with id: ";
    private static final String NOT_FOUND_CV = "Not found cv with id: ";
    private static final String INVALID_PERIOD = "From date must be lower than To date.";
    private static final String DEFAULT_SUBJECT = "Default subject";

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private EducationEntityMapper mapper;

    @Autowired
    private QualificationService qualificationService;

    @Autowired
    private CvService cvService;

    @Override
    public void createNewEducation(EducationDTO dto) {
        EducationEntity entity = new EducationEntity();
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        mapper.DTOToEntity(dto, entity);

        if (qualificationService.getCountQualificationById(dto.getQualificationId()) == 0) {
            throw new NoSuchElementException(NOT_FOUND_QUALIFICATION + dto.getQualificationId());
        }


        if (dto.getFromDate() >= dto.getToDate()) {
            throw new IllegalArgumentException(INVALID_PERIOD);
        }

        if (dto.getSubject().isEmpty()) {
            entity.setSubject(DEFAULT_SUBJECT);
        }

        QualificationEntity qualification = new QualificationEntity();
        qualification.setId(dto.getQualificationId());
        entity.setQualification(qualification);

        CvEntity cv = new CvEntity();
        cv.setId(dto.getCvID());
        entity.setCv(cv);

        educationRepository.save(entity);
    }
}
