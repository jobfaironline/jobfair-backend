package org.capstone.job_fair.services.impl.attendant;

import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.account.GenderEntity;
import org.capstone.job_fair.models.entities.account.RoleEntity;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.services.mappers.AttendantEntityMapper;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.repositories.attendant.AttendantRepository;
import org.capstone.job_fair.services.interfaces.attendant.AttendantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class AttendantServiceImpl implements AttendantService {

    @Autowired
    private AttendantEntityMapper mapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private AttendantRepository attendantRepository;

    @Override
    public void createNewAccount(AttendantDTO dto) {
        String id = UUID.randomUUID().toString();
        AttendantEntity attendantEntity = mapper.toEntity(dto);
        AccountEntity accountEntity = attendantEntity.getAccount();
        attendantEntity.setAccountId(id);
        accountEntity.setId(id);
        accountEntity.setPassword(encoder.encode(accountEntity.getPassword()));
        accountEntity.setProfileImageUrl(AccountConstant.DEFAULT_PROFILE_IMAGE_URL);
        accountEntity.setStatus(AccountStatus.ACTIVE);

        RoleEntity role = new RoleEntity();
        role.setId(Role.ATTENDANT.ordinal());
        accountEntity.setRole(role);

        GenderEntity gender = new GenderEntity();
        gender.setId(dto.getAccount().getGender().ordinal());
        accountEntity.setGender(gender);
        attendantRepository.save(attendantEntity);
    }
}
