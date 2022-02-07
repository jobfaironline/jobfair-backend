package org.capstone.job_fair.services.interfaces.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.DecoratedItemDTO;

import java.util.List;
import java.util.Optional;

public interface DecoratedItemService {
    List<DecoratedItemDTO> getAll();

    Optional<DecoratedItemDTO> findById(String id);

    DecoratedItemDTO createNew(DecoratedItemDTO dto);

    void update(DecoratedItemDTO dto);
}
