package org.capstone.job_fair.services.impl.account;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.token.AccountVerifyTokenEntity;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.repositories.token.AccountVerifyTokenEntityRepository;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.token.AccountVerifyTokenService;
import org.capstone.job_fair.services.interfaces.util.MailService;
import org.capstone.job_fair.services.mappers.account.AccountMapper;
import org.capstone.job_fair.utils.AwsUtil;
import org.capstone.job_fair.utils.DomainUtil;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private AccountVerifyTokenService accountVerifyTokenService;

    @Autowired
    private AccountVerifyTokenEntityRepository accountVerifyTokenEntityRepository;


    @Autowired
    private DomainUtil domainUtil;

    @Autowired
    private MailService mailService;

    @Autowired
    private AwsUtil awsUtil;

    @Autowired
    private Clock clock;

    private void validatePaging(int pageSize, int offset) {
        if (offset < DataConstraint.Paging.OFFSET_MIN || pageSize < DataConstraint.Paging.PAGE_SIZE_MIN)
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.INVALID_PAGE_NUMBER));
    }

    @Override
    public Page<AccountDTO> getAllAccounts(String searchValue, int pageSize, int offset, String sortBy, Sort.Direction direction) {
        validatePaging(pageSize, offset);
        return accountRepository.findAllWithSearchValue("%" + searchValue + "%", PageRequest.of(offset, pageSize).withSort(direction, sortBy)).map(accountMapper::toDTO);
    }

    @Override
    public Optional<AccountEntity> getActiveAccountByEmail(String email) {
        Optional<AccountEntity> accountOpt = accountRepository.findByEmail(email);
        if (!accountOpt.isPresent())
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND));
        if (!accountOpt.get().getStatus().equals(AccountStatus.VERIFIED))
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.NOT_VERIRIED));
        return accountOpt;
    }

    @Override
    @Transactional
    public AccountEntity save(AccountEntity account) {
        return accountRepository.save(account);
    }

    @Override
    public Optional<AccountEntity> getActiveAccountById(String id) {
        return accountRepository.findByIdAndStatus(id, AccountStatus.VERIFIED);
    }

    @Override
    public Optional<AccountDTO> getAccountById(String id) {
        Optional<AccountEntity> opt = accountRepository.findById(id);
        if (!opt.isPresent()) {
            return Optional.empty();
        }
        AccountDTO accountDTO = accountMapper.toDTO(opt.get());
        return Optional.of(accountDTO);
    }

    @Override
    public Integer getCountActiveAccountById(String id) {
        return accountRepository.countByIdAndStatus(id, AccountStatus.VERIFIED);
    }


    @Override
    public Integer getCountAccountByEmail(String email) {
        return accountRepository.countByEmail(email);
    }

    @Override
    @Transactional
    public void activateAccount(String id) {
        Optional<AccountEntity> accountOpt = accountRepository.findById(id);
        if (!accountOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND));
        }
        AccountEntity accountEntity = accountOpt.get();
        accountEntity.setStatus(AccountStatus.VERIFIED);
        accountRepository.save(accountEntity);
    }

    @Override
    @Transactional
    public void changePassword(String newPassword, String oldPassword) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!encoder.matches(oldPassword, userDetails.getPassword())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.OLD_PASSWORD_MISMATCH));
        }
        Optional<AccountEntity> accountOpt = accountRepository.findById(userDetails.getId());
        if (!accountOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND));
        }
        AccountEntity entity = accountOpt.get();
        String hashedPassword = encoder.encode(newPassword);
        entity.setPassword(hashedPassword);
        accountRepository.save(entity);
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    @Transactional
    public void sendVerifyAccountEmail(String accountId) {
        AccountEntity entity = accountRepository.getById(accountId);
        if (!entity.getStatus().equals(AccountStatus.REGISTERED))
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.ALREADY_VERIFIED));
        Optional<AccountVerifyTokenEntity> accountVerifyTokenEntityOptional = accountVerifyTokenEntityRepository.getFirstByAccountIdOrderByExpiredTimeDesc(accountId);
        //if there is a token for this account
        //check for its validation
        if (accountVerifyTokenEntityOptional.isPresent()) {
            //if not validate check on expired time
            AccountVerifyTokenEntity lastToken = accountVerifyTokenEntityOptional.get();
            if (!lastToken.getIsInvalidated()) {
                long currentTime = clock.millis();
                if (lastToken.getExpiredTime() < currentTime) {
                    //token is expired
                    accountVerifyTokenService.invalidateTokenById(lastToken.getId());
                } else {
                    throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.VERIFY_ACCOUNT_TOKEN_INTERVAL_ERROR));
                }
            }
        }
        String token = accountVerifyTokenService.createToken(accountId).getId();
        this.mailService.sendMail(entity.getEmail(),
                        MessageUtil.getMessage(MessageConstant.Account.ACCOUNT_VERIFY_MAIL_TITLE),
                        domainUtil.generateCurrentDomain() + ApiEndPoint.Authorization.VERIFY_USER + "/" + accountId + "/" + token)
                .exceptionally(throwable -> {
                    log.error(throwable.getMessage());
                    return null;
                });
    }

    @Override
    @Transactional
    public AccountDTO createNew(AccountDTO dto) {
        AccountEntity accountEntity = accountMapper.toEntity(dto);
        if (accountRepository.countByEmail(dto.getEmail()) != 0) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.EMAIL_EXISTED));
        }
        accountEntity = accountRepository.save(accountEntity);
        return accountMapper.toDTO(accountEntity);
    }

    @Override
    @Transactional
    public AccountDTO updateProfilePicture(String pictureProfileFolder, String id) {
        String url = awsUtil.generateAwsS3AccessString(pictureProfileFolder, id);
        AccountEntity account = accountRepository.getById(id);
        account.setProfileImageUrl(url);
        accountRepository.save(account);
        return accountMapper.toDTO(account);
    }

    @Override
    @Transactional
    public AccountDTO deactivateOwnAccount(String userId) {
        AccountEntity account = accountRepository.getById(userId);
        account.setStatus(AccountStatus.INACTIVE);
        account = accountRepository.save(account);
        return accountMapper.toDTO(account);
    }

    @Override
    @Transactional
    public AccountDTO reactivateOwnAccount(String userId) {
        AccountEntity account = accountRepository.getById(userId);
        account.setStatus(AccountStatus.VERIFIED);
        account = accountRepository.save(account);
        return accountMapper.toDTO(account);
    }


}
