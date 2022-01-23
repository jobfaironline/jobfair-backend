package org.capstone.job_fair.services.impl.attendant;

import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.*;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.*;
import org.capstone.job_fair.models.entities.attendant.cv.*;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.repositories.attendant.AttendantRepository;
import org.capstone.job_fair.repositories.attendant.cv.*;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.attendant.AttendantService;
import org.capstone.job_fair.services.mappers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
public class AttendantServiceImpl implements AttendantService {

    @Autowired
    private AttendantMapper attendantMapper;

    @Autowired
    private AccountEntityMapper accountMapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private AttendantRepository attendantRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public void updateAccount(AttendantDTO dto) {

        if (dto.getAccount() != null && dto.getAccount().getPassword() != null) {
            String hashedPassword = encoder.encode(dto.getAccount().getPassword());
            dto.getAccount().setPassword(hashedPassword);
        }

        String id = dto.getAccount().getId();
        Optional<AttendantEntity> attendantOpt = attendantRepository.findById(id);

        if (attendantOpt.isPresent()) {
            AttendantEntity entity = attendantOpt.get();
            attendantMapper.updateAttendantMapperFromDto(dto, entity);
        }
    }

    @Override
    public Optional<AttendantDTO> getAttendantByEmail(String email) {
        Optional<AccountEntity> accountOpt = accountService.getActiveAccountByEmail(email);
        if (!accountOpt.isPresent()) {
            return Optional.empty();
        }
        Optional<AttendantEntity> attendantOpt = attendantRepository.findById(accountOpt.get().getId());
        if (!attendantOpt.isPresent()) {
            return Optional.empty();
        }
        AttendantDTO dto = attendantMapper.toDTO(attendantOpt.get());
        return Optional.of(dto);
    }

    @Override
    public AttendantEntity createNewAccount(AttendantDTO dto) {

        AttendantEntity attendantEntity = attendantMapper.toEntity(dto);
        AccountEntity accountEntity = attendantEntity.getAccount();
        attendantEntity.setAccountId(dto.getAccount().getId());
        String hashPassword = encoder.encode(dto.getAccount().getPassword());
        accountEntity.setPassword(hashPassword);
        accountEntity.setProfileImageUrl(AccountConstant.DEFAULT_PROFILE_IMAGE_URL);

        return attendantRepository.save(attendantEntity);

    }

    @Override
    public List<AttendantDTO> getAllAttendants() {
        return attendantRepository.findAll().stream().map(entity -> attendantMapper.toDTO(entity)).collect(Collectors.toList());
    }
}
