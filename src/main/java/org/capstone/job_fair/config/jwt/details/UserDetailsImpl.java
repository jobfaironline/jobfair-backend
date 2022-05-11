package org.capstone.job_fair.config.jwt.details;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.models.statuses.AccountStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String email;
    @JsonIgnore
    private String password;
    private String fullname;
    private String id;
    private AccountStatus status;
    private String companyId;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(AccountEntity account) {
        List<GrantedAuthority> authorities = new ArrayList<>(
                Arrays.asList(
                        new SimpleGrantedAuthority(account.getRole().getName())));
        return new UserDetailsImpl(
                account.getEmail(),
                account.getPassword(),
                account.getFullname(),
                account.getId(),
                account.getStatus(),
                null,
                authorities
        );

    }

    public static UserDetailsImpl build(AccountEntity account, CompanyEntity company) {
        UserDetailsImpl userDetails = build(account);
        userDetails.setCompanyId(company.getId());
        return userDetails;
    }

    public AccountStatus getStatus() {
        return status;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean hasRole(Role role) {
        return this.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role.getAuthority()));
    }
}
