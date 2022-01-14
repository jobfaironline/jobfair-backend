package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.ActivityDTO;
import org.capstone.job_fair.models.entities.attendant.cv.ActivityEntity;
import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.capstone.job_fair.repositories.attendant.cv.ActivityRepository;
import org.capstone.job_fair.services.interfaces.attendant.cv.ActivityService;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.mappers.ActivityEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

import static net.bytebuddy.utility.JavaConstant.Dynamic.DEFAULT_NAME;

@Service
public class ActivityServiceImpl implements ActivityService {

    private static final String DATE_PERIOD_ILLEGAL = "From date must be lower than To date";
    private static final String NOT_FOUND_CV = "Not found cv with id: ";
    private static final String DEFAULT_NAME = "default name for activity";
    private static final String DEFAULT_FUNCTION_TITLE = "default function title for activity";
    private static final String DEFAULT_ORGAN = "default organization for activity";

    @Autowired
    private ActivityRepository activityRep;

    @Autowired
    private ActivityEntityMapper mapper;

    @Autowired
    private CvService cvService;

    @Override
    public void createNewActivity(ActivityDTO dto) {


        ActivityEntity entity = new ActivityEntity();
        entity.setId(UUID.randomUUID().toString());
        mapper.DTOToEntity(dto, entity);

        if (dto.getFromDate() >= dto.getToDate()) {
            throw new IllegalArgumentException(DATE_PERIOD_ILLEGAL);
        }


        if (dto.getName().isEmpty()) {
            entity.setName(DEFAULT_NAME);
        }

        if (dto.getFunctionTitle().isEmpty()) {
            entity.setFunctionTitle(DEFAULT_FUNCTION_TITLE);
        }

        if (dto.getOrganization().isEmpty()) {
            entity.setOrganization(DEFAULT_ORGAN);
        }

        CvEntity cv = new CvEntity();
        cv.setId(dto.getCvID());
        entity.setCv(cv);

        activityRep.save(entity);

    }
}
