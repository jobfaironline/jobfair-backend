package org.capstone.job_fair.services;

import org.capstone.job_fair.models.entities.attendant.RoleEntity;

public interface RoleService {
    RoleEntity findById(int id);
}
