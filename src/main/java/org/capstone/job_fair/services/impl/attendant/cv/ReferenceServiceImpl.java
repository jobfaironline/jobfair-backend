package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.ReferenceDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.capstone.job_fair.models.entities.attendant.cv.ReferenceEntity;
import org.capstone.job_fair.repositories.attendant.cv.ReferenceRepository;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.interfaces.attendant.cv.ReferenceService;
import org.capstone.job_fair.services.mappers.ReferenceEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ReferenceServiceImpl implements ReferenceService {

    private static final String NOT_FOUND_CV = "Not found cv with id: ";

    @Autowired
    private ReferenceRepository referenceRepository;

    @Autowired
    private ReferenceEntityMapper mapper;

    @Autowired
    private CvService cvService;

    @Override
    public void createNewReference(ReferenceDTO dto) {
        ReferenceEntity entity = new ReferenceEntity();
        String id = UUID.randomUUID().toString();
        entity.setId(id);

        mapper.DTOToEntity(dto, entity);


        CvEntity cv = new CvEntity();
        cv.setId(dto.getCvID());
        entity.setCv(cv);

        referenceRepository.save(entity);
    }
}
