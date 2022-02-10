package org.capstone.job_fair.services.interfaces.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.LayoutDTO;

import java.util.List;
import java.util.Optional;

public interface LayoutService {
    List<LayoutDTO> getAll();

    Optional<LayoutDTO> findById(String id);

    LayoutDTO createNew(LayoutDTO dto);

    void update(LayoutDTO dto);
}
