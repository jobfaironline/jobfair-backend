package org.capstone.job_fair.services.interfaces.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.ThreeDimensionMediaDTO;

import java.util.List;
import java.util.Optional;

public interface ThreeDimensionMediaService {
    List<ThreeDimensionMediaDTO> getAll();

    Optional<ThreeDimensionMediaDTO> findById(String id);

    ThreeDimensionMediaDTO createNew(ThreeDimensionMediaDTO dto);

    void update(ThreeDimensionMediaDTO dto);
}
