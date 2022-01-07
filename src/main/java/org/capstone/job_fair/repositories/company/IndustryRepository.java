package org.capstone.job_fair.repositories.company;

import org.capstone.job_fair.models.entities.company.IndustryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(path = "industries")
@Transactional
public interface IndustryRepository extends JpaRepository<IndustryEntity, String> {
}