package org.capstone.job_fair.repositories.company.misc;

import org.capstone.job_fair.models.entities.company.misc.SkillTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillTagRepository extends JpaRepository<SkillTagEntity, Integer> {
}
