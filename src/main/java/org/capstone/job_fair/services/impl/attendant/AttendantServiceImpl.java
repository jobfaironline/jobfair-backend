package org.capstone.job_fair.services.impl.attendant;

import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.repositories.attendant.AttendantRepository;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.attendant.AttendantService;
import org.capstone.job_fair.services.interfaces.attendant.CountryService;
import org.capstone.job_fair.services.interfaces.attendant.ResidenceService;
import org.capstone.job_fair.services.mappers.attendant.AttendantMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class AttendantServiceImpl implements AttendantService {

    @Autowired
    private AttendantMapper attendantMapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private AttendantRepository attendantRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ResidenceService residenceService;


    private boolean isEmailExist(String email) {
        return accountService.getCountAccountByEmail(email) != 0;
    }

    private boolean isCountryExist(String id) {
        return countryService.getCountCountryById(id) != 0;
    }

    private boolean isResidenceExist(String id) {
        return residenceService.getCountResidenceById(id) != 0;
    }


    @Override
    @Transactional
    public void updateAccount(AttendantDTO dto) {
        Optional<AccountEntity> opt = accountService.getActiveAccountById(dto.getAccount().getId());
        if (!opt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND));
        }

        if (dto.getAccount() != null //request must have account
                && dto.getAccount().getEmail() != null //then have email in account
                && !opt.get().getEmail().equals(dto.getAccount().getEmail())
                && isEmailExist(dto.getAccount().getEmail())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.EMAIL_EXISTED));
        }

        if (dto.getCountryId() != null && !isCountryExist(dto.getCountryId())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND_COUNTRY));
        }

        if (dto.getResidenceId() != null && !isResidenceExist(dto.getResidenceId())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND_RESIDENCE));
        }

        if (dto.getAccount() != null && dto.getAccount().getPassword() != null) {
            String hashedPassword = encoder.encode(dto.getAccount().getPassword());
            dto.getAccount().setPassword(hashedPassword);
        }
        String id = dto.getAccount().getId();
        Optional<AttendantEntity> attendantOpt = attendantRepository.findById(id);

        if (attendantOpt.isPresent()) {
            AttendantEntity entity = attendantOpt.get();
            attendantMapper.updateAttendantMapperFromDto(dto, entity);
            attendantRepository.save(entity);
        }
    }

    @Override
    @Transactional
    public AttendantDTO createNewAccount(AttendantDTO dto) {
        if (isEmailExist(dto.getAccount().getEmail())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.EMAIL_EXISTED));
        }

        dto.getAccount().setRole(Role.ATTENDANT);
        dto.getAccount().setStatus(AccountStatus.REGISTERED);
        String hashPassword = encoder.encode(dto.getAccount().getPassword());
        dto.getAccount().setPassword(hashPassword);
        dto.getAccount().setProfileImageUrl(AccountConstant.DEFAULT_PROFILE_IMAGE_URL);
        Long currentTime = new Date().getTime();
        dto.getAccount().setCreateTime(currentTime);

        AttendantEntity attendantEntity = attendantMapper.toEntity(dto);
        attendantEntity.setAccountId(dto.getAccount().getId());
        attendantEntity = attendantRepository.save(attendantEntity);

        return attendantMapper.toDTO(attendantEntity);

    }

    @Override
    public List<AttendantDTO> getAllAttendants() {
        return attendantRepository.findAll().stream().map(entity -> attendantMapper.toDTO(entity)).collect(Collectors.toList());
    }

    @Override
    public Optional<AttendantDTO> getAttendantById(String id) {
        Optional<AttendantEntity> opt = attendantRepository.findById(id);
        if (!opt.isPresent()) {
            return Optional.empty();
        }
        AttendantEntity entity = opt.get();
        AttendantDTO dto = attendantMapper.toDTO(entity);
        return Optional.of(dto);
    }
}
