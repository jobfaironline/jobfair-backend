package org.capstone.job_fair.repositories.account;

import org.capstone.job_fair.models.entities.account.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
}
