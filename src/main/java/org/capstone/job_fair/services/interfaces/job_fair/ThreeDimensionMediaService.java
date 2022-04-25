package org.capstone.job_fair.services.interfaces.job_fair;

import org.capstone.job_fair.models.dtos.job_fair.ThreeDimensionMediaDTO;
import org.capstone.job_fair.models.statuses.ThreeDimensionMediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ThreeDimensionMediaService {
    List<ThreeDimensionMediaDTO> getAll();

    Optional<ThreeDimensionMediaDTO> findById(String id);

    ThreeDimensionMediaDTO createNew(ThreeDimensionMediaDTO dto);

    void update(ThreeDimensionMediaDTO dto);

    Page<ThreeDimensionMediaDTO> findByType(ThreeDimensionMediaType type, int offset, int pageSize, String sortBy, Sort.Direction direction);

    ThreeDimensionMediaDTO createOrUpdateThumbnail(String decoratedThumbnailsFolder, String id);
}
