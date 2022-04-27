package org.capstone.job_fair.services.interfaces.account;

import org.capstone.job_fair.models.dtos.account.RoleDTO;
import org.capstone.job_fair.models.dtos.attendant.profile.QualificationDTO;
import org.capstone.job_fair.models.entities.account.RoleEntity;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<RoleDTO> findById(int id);

    List<RoleDTO> getAll();

    RoleDTO delete(int id);

    RoleDTO create(RoleDTO dto);

    RoleDTO update(RoleDTO dto);
}
