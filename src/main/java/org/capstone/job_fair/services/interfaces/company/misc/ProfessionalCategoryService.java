package org.capstone.job_fair.services.interfaces.company.misc;

import org.capstone.job_fair.models.dtos.company.misc.ProfessionCategoryDTO;

import java.util.List;
import java.util.Optional;

public interface ProfessionalCategoryService {
    Optional<ProfessionCategoryDTO> findById(int id);

    List<ProfessionCategoryDTO> getAll();

    ProfessionCategoryDTO delete(int id);

    ProfessionCategoryDTO create(ProfessionCategoryDTO dto);

    ProfessionCategoryDTO update(ProfessionCategoryDTO dto);
}
