package org.capstone.job_fair.services.impl.attendant.cv;

import org.capstone.job_fair.models.dtos.attendant.cv.SkillDTO;
import org.capstone.job_fair.models.entities.attendant.cv.CvEntity;
import org.capstone.job_fair.models.entities.attendant.cv.SkillEntity;
import org.capstone.job_fair.repositories.attendant.cv.SkillRepository;
import org.capstone.job_fair.services.interfaces.attendant.cv.CvService;
import org.capstone.job_fair.services.interfaces.attendant.cv.SkillService;
import org.capstone.job_fair.services.mappers.SkillEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class SkillServiceImpl implements SkillService{

    private static final String NOT_FOUND_CV = "Not found CV by id: ";
    private static final String PROFICIENCY_INVALID = "Proficiency must be greater than 0";

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillEntityMapper mapper;

    @Autowired
    private CvService cvService;

    @Override
    public void createSkill(SkillDTO dto) {
        SkillEntity entity = new SkillEntity();
        String id = UUID.randomUUID().toString();
        mapper.DTOToEntity(dto, entity);
        entity.setId(id);


        if (dto.getProficiency() < 1) {
            throw new IllegalArgumentException(PROFICIENCY_INVALID);
        }


        CvEntity cv = new CvEntity();
        cv.setId(dto.getCvID());
        entity.setCv(cv);


        skillRepository.save(entity);
    }
}
