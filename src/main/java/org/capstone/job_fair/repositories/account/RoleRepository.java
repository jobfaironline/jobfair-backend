package org.capstone.job_fair.repositories.account;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.entities.account.RoleEntity;
import org.capstone.job_fair.repositories.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RepositoryRestResource(path = ApiEndPoint.RestDataEndpoint.ROLE)
public interface RoleRepository extends CustomRepository<RoleEntity, Integer>, JpaRepository<RoleEntity, Integer> {
}
