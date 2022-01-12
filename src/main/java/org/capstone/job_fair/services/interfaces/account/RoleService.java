package org.capstone.job_fair.services.interfaces.account;

import org.capstone.job_fair.models.entities.account.RoleEntity;

public interface RoleService {
    RoleEntity findById(int id);
}
