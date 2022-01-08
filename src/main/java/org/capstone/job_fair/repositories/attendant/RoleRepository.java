package org.capstone.job_fair.repositories.attendant;

import org.capstone.job_fair.models.entities.attendant.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RepositoryRestResource(path = "roles")
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
}
