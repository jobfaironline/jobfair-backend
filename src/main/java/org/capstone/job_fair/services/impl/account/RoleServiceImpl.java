package org.capstone.job_fair.services.impl.account;

import org.capstone.job_fair.models.entities.account.RoleEntity;
import org.capstone.job_fair.repositories.account.RoleRepository;
import org.capstone.job_fair.services.interfaces.account.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Override
    public RoleEntity findById(int id) {
        Optional<RoleEntity> opt = roleRepository.findById(id);
        return opt.isPresent() ? opt.get() : null;
    }
}
