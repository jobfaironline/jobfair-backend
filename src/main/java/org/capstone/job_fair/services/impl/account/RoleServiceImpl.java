package org.capstone.job_fair.services.impl.account;

import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.account.RoleDTO;
import org.capstone.job_fair.models.entities.account.RoleEntity;
import org.capstone.job_fair.repositories.account.RoleRepository;
import org.capstone.job_fair.services.interfaces.account.RoleService;
import org.capstone.job_fair.services.mappers.account.RoleMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Optional<RoleDTO> findById(int id) {
        return roleRepository.findById(id).map(entity -> roleMapper.toDTO(entity));
    }

    @Override
    public List<RoleDTO> getAll() {
        return roleRepository.findAll().stream().map(entity -> roleMapper.toDTO(entity)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoleDTO delete(int id) {
        Optional<RoleEntity> roleEntityOptional = roleRepository.findById(id);
        if(!roleEntityOptional.isPresent()) throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Role.NOT_FOUND));
        roleRepository.deleteById(id);
        return roleMapper.toDTO(roleEntityOptional.get());
    }

    @Override
    @Transactional
    public RoleDTO create(RoleDTO dto) {
        Optional<RoleEntity> roleEntityOptional = roleRepository.findById(dto.getId());
        if(roleEntityOptional.isPresent()) throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Role.DUPLICATED));
        RoleEntity entity = roleRepository.save(roleMapper.toEntity(dto));
        return roleMapper.toDTO(entity);
    }

    @Override
    @Transactional
    public RoleDTO update(RoleDTO dto) {
        Optional<RoleEntity> roleEntityOptional = roleRepository.findById(dto.getId());
        if(!roleEntityOptional.isPresent()) throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Role.NOT_FOUND));
        RoleEntity entity = roleRepository.save(roleMapper.toEntity(dto));
        return roleMapper.toDTO(entity);
    }
}
