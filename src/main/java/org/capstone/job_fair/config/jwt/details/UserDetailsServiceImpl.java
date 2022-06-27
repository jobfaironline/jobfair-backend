package org.capstone.job_fair.config.jwt.details;

import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.company.CompanyEmployeeEntity;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.capstone.job_fair.repositories.account.AccountRepository;
import org.capstone.job_fair.repositories.company.CompanyEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CompanyEmployeeRepository companyEmployeeRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AccountEntity account = accountRepository
                .findByEmailAndStatusIn(email, Arrays.asList(AccountStatus.VERIFIED, AccountStatus.REGISTERED, AccountStatus.INACTIVE ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with -> email : " + email));
        if (account.getRole().getId() == Role.COMPANY_MANAGER.ordinal() || account.getRole().getId() == Role.COMPANY_EMPLOYEE.ordinal()) {
            CompanyEmployeeEntity companyEmployee = companyEmployeeRepository
                    .findByAccountId(account.getId())
                    .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Company not found for this account"));
            return UserDetailsImpl.build(account, companyEmployee.getCompany());
        }

        return UserDetailsImpl.build(account);
    }
}
