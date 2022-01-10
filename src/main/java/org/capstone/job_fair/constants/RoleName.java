package org.capstone.job_fair.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleName {

    ADMIN("admin"),
    CUSTOMER("customer");

    private String displayRole;

    RoleName(String displayRole){
        this.displayRole = displayRole;
    }

    @JsonValue
    public String getDisplayRole() {
        return displayRole;
    }

    public void setDisplayRole(String displayRole) {
        this.displayRole = displayRole;
    }

}