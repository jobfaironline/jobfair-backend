package org.capstone.job_fair.repositories.attendant.misc;

import org.capstone.job_fair.models.entities.attendant.misc.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<LanguageEntity, String> {
}
