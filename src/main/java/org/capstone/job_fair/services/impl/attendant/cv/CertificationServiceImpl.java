package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.CertificationDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CertificationEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.capstone.job_fair.repositories.attendant.cv.CertificationRepository;
import org.capstone.job_fair.services.interfaces.attendant.cv.CertificationService;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.mappers.CertificationEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class CertificationServiceImpl implements CertificationService {

    private static final String YEAR_EXCEPTION = "Year must be greater than 1 and lower than current year";
    private static final String NOT_FOUND_CV = "Not found CV with id: ";
    private static final String DEFAULT_NAME = "default name for certification.";

    @Autowired
    private CertificationRepository certificationRepository;


    @Autowired
    private CertificationEntityMapper mapper;

    @Autowired
    private CvService cvService;

    @Override
    public void createNewCertification(CertificationDTO dto) {
        CertificationEntity entity = new CertificationEntity();
        String id = UUID.randomUUID().toString();
        entity.setId(id);

        mapper.DTOToEntity(dto, entity);

        if (dto.getYear() < 1 || dto.getYear() > Calendar.getInstance().get(Calendar.YEAR)) {
            throw new IllegalArgumentException(YEAR_EXCEPTION);
        }

//        if (cvService.getCountCvByID(dto.getCvID()) == 0) {
//            throw new NoSuchElementException(NOT_FOUND_CV + dto.getCvID());
//        }

        if (dto.getName().isEmpty()) {
            entity.setName(DEFAULT_NAME);
        }

        CvEntity cv = new CvEntity();
        cv.setId(dto.getCvID());
        entity.setCv(cv);

        certificationRepository.save(entity);
    }
}
