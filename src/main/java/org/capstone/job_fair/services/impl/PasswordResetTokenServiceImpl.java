package org.capstone.job_fair.services.impl;

import org.capstone.job_fair.constants.AccountConstant;
import org.capstone.job_fair.models.entities.AccountEntity;
import org.capstone.job_fair.models.entities.PasswordResetTokenEntity;
import org.capstone.job_fair.repositories.AccountRepository;
import org.capstone.job_fair.repositories.PasswordResetTokenRepository;
import org.capstone.job_fair.services.AccountService;
import org.capstone.job_fair.services.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository resetRepository;

    @Autowired
    private AccountService accountService;


    @Override
    public PasswordResetTokenEntity findTokenByEmail(String email) {

        Optional<AccountEntity> optAccount = accountService.getActiveAccountByEmail(email);
        if (optAccount.isPresent()) {
            PasswordResetTokenEntity result = resetRepository.findByAccount_Id(optAccount.get().getId().toString());
            return result;
        }
        return null;
    }

    @Override
    public Boolean saveToken(PasswordResetTokenEntity token) {
        if (token != null) {
            resetRepository.save(token);
            return true;
        }
        return false;
    }
}
