package org.capstone.job_fair.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    STAFF,
    COMPANY_MANAGER,
    COMPANY_EMPLOYEE,
    ATTENDANT;

    @Override
    public String getAuthority() {
        return this.name();
    }



}

