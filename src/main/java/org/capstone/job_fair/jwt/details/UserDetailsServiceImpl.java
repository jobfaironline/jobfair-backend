package org.capstone.job_fair.jwt.details;

import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Autowired
    public UserDetailsServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AccountEntity account = accountRepository
                .findByEmailAndStatusNot(email, AccountStatus.ACTIVE)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with -> email : " + email));
        return UserDetailsImpl.build(account);
    }
}
