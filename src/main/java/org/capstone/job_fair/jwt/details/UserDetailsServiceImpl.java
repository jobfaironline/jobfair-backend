package org.capstone.job_fair.jwt.details;

import org.capstone.job_fair.models.entities.attendant.AttendantEntity;
import org.capstone.job_fair.repositories.attendant.AttendantRepository;
import org.capstone.job_fair.constants.AccountConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AttendantRepository attendantRepository;

    @Autowired
    public UserDetailsServiceImpl(AttendantRepository attendantRepository) {
        this.attendantRepository = attendantRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AttendantEntity account = attendantRepository
                .findAccountByEmailAndStatusNot(email, AccountConstant.INACTIVE)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with -> email : " + email));
        return UserDetailsImpl.build(account);
    }
}
