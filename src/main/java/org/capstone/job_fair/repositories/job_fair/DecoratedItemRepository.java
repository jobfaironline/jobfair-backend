package org.capstone.job_fair.repositories.job_fair;

import org.capstone.job_fair.models.entities.job_fair.ThreeDimensionMedia;
import org.capstone.job_fair.models.statuses.ThreeDimensionMediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecoratedItemRepository extends JpaRepository<ThreeDimensionMedia, String> {
    Page<ThreeDimensionMedia> findAllByType(ThreeDimensionMediaType type, Pageable pageable);
}
