package org.capstone.job_fair.jwt.details;

import org.capstone.job_fair.models.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
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
    private int status;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(Account account) {
        List<GrantedAuthority> authorities = new ArrayList<>(
                Arrays.asList(
                        new SimpleGrantedAuthority(account.getRole().getName().toString())));
        return new UserDetailsImpl(
                account.getEmail(),
                account.getPassword(),
                account.getFullname(),
                account.getStatus(),
                authorities
        );

    }

    public int getStatus() {
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
}
