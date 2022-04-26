package org.capstone.job_fair.services.interfaces.company;

import org.capstone.job_fair.models.dtos.company.SkillTagDTO;

import java.util.List;
import java.util.Optional;

public interface SkillService {
    Optional<SkillTagDTO> findById(int id);

    List<SkillTagDTO> getAll();

    SkillTagDTO delete(int id);

    SkillTagDTO create(SkillTagDTO dto);

    SkillTagDTO update(SkillTagDTO dto);
}
