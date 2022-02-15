package org.capstone.job_fair.services.impl.account;

import lombok.SneakyThrows;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.token.AccountVerifyTokenDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.repositories.token.AccountVerifyTokenEntityRepository;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.token.AccountVerifyTokenService;
import org.capstone.job_fair.services.interfaces.util.MailService;
import org.capstone.job_fair.services.mappers.AccountMapper;
import org.capstone.job_fair.utils.AwsUtil;
import org.capstone.job_fair.utils.DomainUtil;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
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
    private DomainUtil domainUtil;

    @Autowired
    private MailService mailService;

    @Autowired
    private AwsUtil awsUtil;

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll()
                .stream().map(AccountEntity -> accountMapper.toDTO(AccountEntity))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AccountEntity> getActiveAccountByEmail(String email) {
        return accountRepository.findByEmailAndStatus(email, AccountStatus.VERIFIED);
    }

    @Override
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
    public void activateAccount(String id) {
        AccountEntity accountEntity = accountRepository.findById(id).get();
        accountEntity.setStatus(AccountStatus.VERIFIED);
        accountRepository.save(accountEntity);
    }

    @Override
    public String getIdByEmail(String email) {
        String id = accountRepository.findByEmail(email).get().getId().toString();
        return id;
    }

    @Override
    @Transactional
    public void changePassword(String newPassword, String oldPassword) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!encoder.matches(oldPassword, userDetails.getPassword())) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.OLD_PASSWORD_MISMATCH));
        }
        AccountEntity entity = accountRepository.findById(userDetails.getId()).get();
        String hashedPassword = encoder.encode(newPassword);
        entity.setPassword(hashedPassword);
        accountRepository.save(entity);
    }

    @Override
    @SneakyThrows
    public void sendVerifyAccountEmail(String accountId){
        AccountEntity entity = accountRepository.getById(accountId);
        AccountVerifyTokenDTO lastTokenDTO = accountVerifyTokenService.getLastedToken(accountId);
        //if there is a token for this account
        //check for its validation
        if (lastTokenDTO != null){
            //if not validate check on expired time
            if (!lastTokenDTO.getIsInvalidated()){
                long currentTime = new Date().getTime();
                if (lastTokenDTO.getExpiredTime() < currentTime){
                    //token is expired
                    accountVerifyTokenService.invalidateTokenById(lastTokenDTO.getId());
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
                    throwable.printStackTrace();
                    return null;
                });;

    }

    @Override
    public AccountDTO createNew(AccountDTO dto) {
        AccountEntity accountEntity = accountMapper.toEntity(dto);
        if (accountRepository.countByEmail(dto.getEmail()) != 0) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Account.EMAIL_EXISTED));
        }
        accountEntity = accountRepository.save(accountEntity);
        return accountMapper.toDTO(accountEntity);
    }

    @Override
    public AccountDTO updateProfilePicture(String pictureProfileFolder) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String id = userDetails.getId();
        String url = awsUtil.generateAwsS3AccessString(pictureProfileFolder, id);
        AccountEntity account = accountRepository.getById(id);
        account.setProfileImageUrl(url);
        accountRepository.save(account);
        return accountMapper.toDTO(account);
    }

}
