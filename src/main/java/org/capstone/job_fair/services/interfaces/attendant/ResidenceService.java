package org.capstone.job_fair.services.interfaces.attendant;

import org.capstone.job_fair.models.dtos.attendant.ResidenceDTO;

import java.util.List;
import java.util.Optional;

public interface ResidenceService {
    Integer getCountResidenceById(String id);

    Optional<ResidenceDTO> findById(String id);

    List<ResidenceDTO> getAll();

    ResidenceDTO delete(String id);

    ResidenceDTO create(ResidenceDTO dto);

    ResidenceDTO update(ResidenceDTO dto);
}
