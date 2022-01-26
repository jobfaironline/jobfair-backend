package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.company.SkillTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.SKILL_TAG)
public interface SkillTagRepository extends JpaRepository<SkillTagEntity, Integer> {
}
