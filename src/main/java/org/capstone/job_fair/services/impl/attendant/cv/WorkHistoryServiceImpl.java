package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.WorkHistoryDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.capstone.job_fair.models.entities.attendant.cv.WorkHistoryEntity;
import org.capstone.job_fair.repositories.attendant.cv.WorkHistoryRepository;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.interfaces.attendant.cv.WorkHistoryService;
import org.capstone.job_fair.services.mappers.WorkHistoryEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class WorkHistoryServiceImpl implements WorkHistoryService {

    private static final String NOT_FOUND_CV = "Not found cv with id: ";
    private static final String INVALID_PERIOD = "From date must be lower than To date ";

    @Autowired
    private WorkHistoryRepository workHistoryRepository;

    @Autowired
    private WorkHistoryEntityMapper mapper;

    @Autowired
    private CvService cvService;

    @Override
    public void createWorkHistory(WorkHistoryDTO dto) {
        WorkHistoryEntity entity = new WorkHistoryEntity();
        String id = UUID.randomUUID().toString();
        entity.setId(id);

        mapper.DTOToEntity(dto,entity);


        if (dto.getFromDate() >= dto.getToDate()) {
            throw new NoSuchElementException(INVALID_PERIOD);
        }

        CvEntity cvEntity = new CvEntity();
        cvEntity.setId(dto.getCvID());
        entity.setCv(cvEntity);

        workHistoryRepository.save(entity);

    }
}
