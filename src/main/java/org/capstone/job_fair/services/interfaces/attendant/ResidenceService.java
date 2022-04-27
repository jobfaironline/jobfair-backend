package org.capstone.job_fair.services.interfaces.attendant;

import org.capstone.job_fair.models.dtos.attendant.ResidenceDTO;

import java.util.List;
import java.util.Optional;

public interface ResidenceService {
    Integer getCountResidenceById(int id);

    Optional<ResidenceDTO> findById(int id);

    List<ResidenceDTO> getAll();

    ResidenceDTO delete(int id);

    ResidenceDTO create(ResidenceDTO dto);

    ResidenceDTO update(ResidenceDTO dto);
}
