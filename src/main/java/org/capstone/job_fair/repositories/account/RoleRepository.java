package org.capstone.job_fair.repositories.account;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.account.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
}
